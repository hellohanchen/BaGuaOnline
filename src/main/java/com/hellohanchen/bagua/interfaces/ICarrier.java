package com.hellohanchen.bagua.interfaces;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.Player;

import java.util.List;

public interface ICarrier<T> extends Identifiable {
    GameManager getGameManager();

    Player getOwner();

    List<T> getInner();

    GameObject asGameObject();
}

