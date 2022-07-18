package com.hellohanchen.baguaserver.controller;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.baguaserver.entity.PlayerId;
import com.hellohanchen.baguaserver.game.GameManagerServer;
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
        logger.info("BaGua Online - user {} start matching...", user.getName());

        PlayerId player1 = new PlayerId(user.getName());

        if (playerGames.findById(player1) != null) {
            responseInvalidRequest("A game is already running for user", user);
        } else {

            pendingPlayers.save(player1);

            if (pendingPlayers.count() > 1) {
                pendingPlayers.delete(player1);
                PlayerId player2 = pendingPlayers.findAll(0, 1).get(0);
                pendingPlayers.delete(player2);

                GameManagerServer game = new GameManagerServer(
                        player1.userName(),
                        player2.userName(),
                        appResponseFactory);

                games.save(game);
                playerGames.add(player1, game.getId());
                playerGames.add(player2, game.getId());

                appResponseFactory.newObjectResponse()
                        .command(MATCH_GAME)
                        .param("gameId", game.getId())
                        .param("player", "1")
                        .usernames(player1.userName())
                        .execute();
                appResponseFactory.newObjectResponse()
                        .command(MATCH_GAME)
                        .param("gameId", game.getId())
                        .param("player", "2")
                        .usernames(player2.userName())
                        .execute();

                game.process();
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
