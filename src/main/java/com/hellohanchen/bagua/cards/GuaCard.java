package com.hellohanchen.bagua.cards;

import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.enums.CardType;
import com.hellohanchen.bagua.exceptions.GameObjectException;
import com.hellohanchen.bagua.guas.SingularGua;
import com.hellohanchen.bagua.statics.CardFactory;
import com.hellohanchen.bagua.statics.EffectFactory;
import lombok.Getter;

/**
 * GuaCard is the most common type of cards in this game.
 */
public class GuaCard extends Card {
    @Getter
    private SingularGua gua; // basic Gua Card must have a singular Gua

    public GuaCard(SingularGua gua) {
        this.gua = gua;
        this.name = gua.toString();
        this.description = CardFactory.generateCardDescription(gua);
        this.effects = EffectFactory.generateEffects(gua);
        this.type = CardType.Gua;
    }

    public void initiate(SingularGua gua) {
        this.gua = gua;
        this.name = gua.toString();
        this.description = CardFactory.generateCardDescription(gua);
        this.effects = EffectFactory.generateEffects(gua);
        this.type = CardType.Gua;
    }

    @Override
    public void copyFrom(Card other) {
        if (!(other instanceof GuaCard)) {
            throw new GameObjectException(
                    "Error: Trying to copy GuaCard from " + other.getClass());
        }

        initiate(((GuaCard) other).gua);
    }

    /* Getters */

    @Override
    public int getLevelValue() {
        return getGua().getLevelValue();
    }

    @Override
    public int getCardCode() {
        return getGua().getElement().ordinal();
    }

    @Override
    public GuaCard getCopy() {
        return new GuaCard(getGua());
    }
}
