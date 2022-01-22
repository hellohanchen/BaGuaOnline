package com.hellohanchen.baguaserver.controller;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.baguaserver.entity.PlayerId;
import com.hellohanchen.baguaserver.repo.GameRepo;
import com.hellohanchen.baguaserver.repo.PendingPlayerRepo;
import com.hellohanchen.baguaserver.repo.PlayerGameRepo;
import com.hellohanchen.baguaserver.request.MatchRequest;
import com.hellohanchen.baguaserver.response.InvalidRequestResponse;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import lombok.Setter;

import java.util.Arrays;

import static com.hellohanchen.baguaserver.request.RequestCmd.INVALID_REQUEST;
import static com.hellohanchen.baguaserver.request.RequestCmd.MATCH_GAME;

@Setter
@EzyRequestController
public class RequestController extends EzyLoggable {

    @EzyAutoBind
    private EzyResponseFactory appResponseFactory;

    @EzyAutoBind
    private GameRepo games;

    @EzyAutoBind
    private PlayerGameRepo playerGames;

    @EzyAutoBind
    private PendingPlayerRepo pendingPlayers;

    @EzyDoHandle(MATCH_GAME)
    public synchronized void match(MatchRequest request, EzyUser user) {
        PlayerId p1 = new PlayerId(user.getName());

        if (playerGames.findById(p1) != null) {
            responseInvalidRequest("A game is already running for user", user);
        } else {

            pendingPlayers.save(p1);

            if (pendingPlayers.count() > 1) {
                pendingPlayers.delete(p1);
                PlayerId p2 = pendingPlayers.findAll(0, 1).get(0);
                pendingPlayers.delete(p2);

                GameManager game = new GameManager(p1.userName(), p2.userName());
                games.save(game);
                playerGames.add(p1, game.getId());
                playerGames.add(p2, game.getId());

                appResponseFactory.newObjectResponse()
                        .command(MATCH_GAME)
                        .param("gameId", game.getId())
                        .param("player", "1")
                        .usernames(p1.userName())
                        .execute();
                appResponseFactory.newObjectResponse()
                        .command(MATCH_GAME)
                        .param("gameId", game.getId())
                        .param("player", "2")
                        .usernames(p2.userName())
                        .execute();
            } else {
                appResponseFactory.newObjectResponse()
                        .command(MATCH_GAME)
                        .param("gameId", "")
                        .user(user)
                        .execute();
            }
        }
    }

    private void responseInvalidRequest(String message, EzyUser user) {
        appResponseFactory.newObjectResponse()
                .command(INVALID_REQUEST)
                .user(user)
                .data(new InvalidRequestResponse(message))
                .execute();
    }
}
