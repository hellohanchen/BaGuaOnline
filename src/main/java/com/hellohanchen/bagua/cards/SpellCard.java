package com.hellohanchen.bagua.cards;

import com.hellohanchen.bagua.exceptions.GameObjectException;
import com.hellohanchen.baguaserver.entity.GameStatus;
import com.hellohanchen.baguaserver.entity.GameStatus.CardData;
import lombok.Getter;

public class SpellCard extends Card {
    @Getter
    private int cardCode;

    public SpellCard(String name, String description, int cardCode) {
        this.initiate(name, description, cardCode);
    }

    public void initiate(String name, String description, int cardCode) {
        this.name = name;
        this.description = description;
        this.cardCode = cardCode;
    }

    @Override
    public void copyFrom(Card other) {
        if (!(other instanceof SpellCard)) {
            throw new GameObjectException(
                    "Error: Trying to copy SpellCard from " + other.getClass());
        }

        initiate(other.name, other.description, ((SpellCard) other).cardCode);
    }

    /* Getters */
    @Override
    public int getLevelValue() {
        return -1;
    }

    @Override
    public SpellCard getCopy() {
        return new SpellCard(this.name, this.description, this.cardCode);
    }
}
