package com.hellohanchen.bagua.predicates.cardpredicates;

import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.predicates.IPredicate;

public abstract class CardPredicate implements IPredicate<Card> {
    public abstract boolean check(Card card);
}
