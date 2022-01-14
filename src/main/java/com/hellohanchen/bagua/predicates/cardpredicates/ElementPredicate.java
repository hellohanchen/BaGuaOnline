package com.hellohanchen.bagua.predicates.cardpredicates;

import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.cards.GuaCard;
import com.hellohanchen.bagua.enums.Element;

import java.util.Collection;
import java.util.HashSet;

public class ElementPredicate extends CardPredicate {
    public final HashSet<Element> elements;

    public ElementPredicate(Collection<Element> elements) {
        this.elements = new HashSet<>(elements);
    }

    @Override
    public boolean check(Card card) {
        return card instanceof GuaCard guaCard
                && elements.contains(guaCard.getGua().getElement());
    }
}
