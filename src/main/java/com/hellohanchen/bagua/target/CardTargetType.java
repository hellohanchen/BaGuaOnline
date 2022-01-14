package com.hellohanchen.bagua.target;

import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.enums.TargetSelectMethod;
import com.hellohanchen.bagua.interfaces.ICopy;
import com.hellohanchen.bagua.predicates.cardpredicates.CardPredicate;

/**
 * CardTargetType represents a card selection method.
 */
public record CardTargetType(
        CardPredicate predicate,
        TargetSelectMethod targetSelectMethod,
        DynamicInt numberToSelect) implements ICopy<CardTargetType> {

    @Override
    public CardTargetType getCopy() {
        return new CardTargetType(predicate, targetSelectMethod, numberToSelect.getCopy());
    }
}
