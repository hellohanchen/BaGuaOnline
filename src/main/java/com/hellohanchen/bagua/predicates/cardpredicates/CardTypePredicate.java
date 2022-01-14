package com.hellohanchen.bagua.predicates.cardpredicates;

import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.cards.GuaCard;
import com.hellohanchen.bagua.enums.CardType;
import com.hellohanchen.bagua.enums.Element;

import java.util.Collection;
import java.util.HashSet;

public class CardTypePredicate extends CardPredicate {
    public final HashSet<CardType> types;

    public CardTypePredicate(Collection<CardType> types) {
        this.types = new HashSet<>(types);
    }

    @Override
    public boolean check(Card card) {
        return types.contains(CardType.General) || types.contains(card.getType());
    }
}
