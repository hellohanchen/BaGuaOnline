package com.hellohanchen.bagua.cards;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.Player;
import com.hellohanchen.bagua.effects.Effect;
import com.hellohanchen.bagua.enums.CardType;
import com.hellohanchen.bagua.interfaces.ICopy;
import com.hellohanchen.baguaserver.entity.GameStatus.CardData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class Card implements ICopy<Card> {
    @Getter
    protected CardType type;
    @Getter
    protected List<Effect> effects;
    @Getter
    private Player owner;
    @Getter
    private GameManager gameManager;
    @Getter
    protected String name;
    @Getter
    protected String description;
    @Getter @Setter
    private int positionInPocket;

    public void activate(GameManager gameManager, Player player, int positionInPocket) {
        this.gameManager = gameManager;
        this.owner = player;
        this.positionInPocket = positionInPocket;
    }

    public void activate(GameManager gameManager, Player player) {
        this.activate(
                gameManager,
                player,
                gameManager.getCardHolder(player).pocketCount() + 1
        );
    }

    public abstract void copyFrom(Card other);

    /* Getters */
    public abstract int getCardCode();

    public abstract int getLevelValue();

    public abstract Card getCopy();

    public CardData asData() {
        CardData data = new CardData();
        data.setName(getName());
        data.setDescription(getDescription());
        data.setType(getType().ordinal());

        return data;
    }
}
