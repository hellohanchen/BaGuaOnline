package com.hellohanchen.bagua.predicates.cardpredicates;

import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.dynamicints.DynamicInt;

/**
 * CardPositionPredicate is looking for the card at a specific position in the pocket.
 */
public class PositionPredicate extends CardPredicate {
    private final DynamicInt position;

    public PositionPredicate(DynamicInt position) {
        this.position = position;
    }

    @Override
    public boolean check(Card card) {
        return card.getPositionInPocket() == position.getCurrentValue();
    }
}
