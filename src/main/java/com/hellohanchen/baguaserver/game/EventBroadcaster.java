package com.hellohanchen.baguaserver.game;

import com.hellohanchen.bagua.Player;
import com.hellohanchen.bagua.cardholders.PocketLibrary;
import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.enums.EventType;
import com.hellohanchen.baguaserver.entity.GameEvent;
import com.hellohanchen.baguaserver.entity.GameId;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hellohanchen.baguaserver.request.RequestCmd.BROADCAST_EVENT;

public class EventBroadcaster {
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    private final GameId gameId;
    private final EzyResponseFactory appResponseFactory;

    public EventBroadcaster(GameId gameId, EzyResponseFactory appResponseFactory) {
        this.gameId = gameId;
        this.appResponseFactory = appResponseFactory;
    }

    public void gameStatus(EventType type, int eventId) {
        logger.info("Broadcast an event with type {%s}".formatted(type));
        appResponseFactory.newObjectResponse()
                .command(BROADCAST_EVENT)
                .param("gameId", gameId)
                .param("player", "1")
                .param("event", GameEvent.builder().type(type).eId(eventId).build())
                .usernames(gameId.getPlayer1())
                .execute();
        appResponseFactory.newObjectResponse()
                .command(BROADCAST_EVENT)
                .param("gameId", gameId)
                .param("player", "2")
                .param("event", GameEvent.builder().type(type).eId(eventId).build())
                .usernames(gameId.getPlayer2())
                .execute();
    }

    public void addCard(EventType type, int eventId, PocketLibrary target, Card card) {
        Player cardPlayer = target.getOwner();
        Player opponent = cardPlayer.getOpponent();

        GameEvent playerEvent = GameEvent.builder()
                .type(type)
                .eId(eventId)
                .pId(cardPlayer.getId())
                .card(card.asData())
                .build();

        appResponseFactory.newObjectResponse()
                .command(BROADCAST_EVENT)
                .param("gameId", gameId)
                .param("player", cardPlayer.getId())
                .param("event", playerEvent)
                .usernames(cardPlayer.getName())
                .execute();

        GameEvent opponentEvent = GameEvent.builder()
                .type(type)
                .eId(eventId)
                .pId(cardPlayer.getId())
                .build();

        appResponseFactory.newObjectResponse()
                .command(BROADCAST_EVENT)
                .param("gameId", gameId)
                .param("player", opponent.getId())
                .param("event", opponentEvent)
                .usernames(opponent.getName())
                .execute();
    }
}
