package com.hellohanchen.bagua;

import com.hellohanchen.bagua.enums.TargetOwnerLogic;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import lombok.Getter;

public class Player {
    @Getter
    private final String name;
    @Getter
    private final int id;
    private Player opponent;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Player applyTargetLogic(TargetOwnerLogic targetOwnerLogic) {
        return switch (targetOwnerLogic) {
            case ALLY -> this;
            case ENEMY -> getOpponent();
            case ANY -> throw new GameProcessException("OwnerType: Can't apply target logic ANY on specific owner type.");
        };
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
        if (this != opponent.getOpponent()) {
            opponent.setOpponent(this);
        }
    }
}
