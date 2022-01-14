package com.hellohanchen.bagua.cardholders;

import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.target.CardTargetType;

import java.util.List;

public interface ICardHolder {
    void addCard(Card card);

    List<Card> getCards();

    List<Card> getCards(CardTargetType targetType);
}
