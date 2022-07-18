package com.hellohanchen.baguaserver.game;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.cardholders.PocketLibrary;
import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.enums.EventType;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;

public class GameManagerServer extends GameManager {
    private final EventBroadcaster broadcaster;

    public GameManagerServer(String name1, String name2, EzyResponseFactory appResponseFactory) {
        super(name1, name2);
        this.broadcaster = new EventBroadcaster(this.getId(), appResponseFactory);
    }

    @Override
    public void addNewEvent(EventType type, GameObject target, GameObject addend) {
        super.addNewEvent(type, target, addend);

        switch (type) {
            case AddCard -> broadcaster.addCard(type, eventTracker.lastId(), (PocketLibrary) target, (Card) addend);
            default -> {
                // TODO
            }
        }
    }

    @Override
    public void addNewEvent(EventType type) {
        super.addNewEvent(type);

        broadcaster.gameStatus(type, eventTracker.lastId());
    }
}
