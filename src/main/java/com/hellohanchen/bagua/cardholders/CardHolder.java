package com.hellohanchen.bagua.cardholders;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.effects.auras.IAuraTarget;
import com.hellohanchen.bagua.enums.TargetSelectMethod;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import com.hellohanchen.bagua.predicates.cardpredicates.CardPredicate;
import com.hellohanchen.bagua.statics.GameUtils;
import com.hellohanchen.bagua.target.CardTargetType;

import java.util.List;

/**
 * AstrolabeCardHolder is for gameObjects that also implement ICardHolder interface.
 */
public abstract class CardHolder extends GameObject implements ICardHolder, IAuraTarget {
    /**
     * Accept a new card.
     * @param card card
     */
    public abstract void addCard(Card card);

    /**
     * @return All cards that are currently held in this collection.
     */
    public abstract List<Card> getCards();

    /**
     * @param targetType CardTargetType that represents selections criterion
     * @return Cards selected based on the targetType.
     */
    public List<Card> getCards(CardTargetType targetType) {
        TargetSelectMethod method = targetType.targetSelectMethod();
        int numberToSelect = targetType.numberToSelect().getCurrentValue(this);
        CardPredicate predicate = targetType.predicate();

        List<Card> validCards = getCards().stream().filter(predicate::check).toList();

        switch (method) {
            case All -> {
                return validCards;
            }
            case Random -> {
                return GameUtils.randomElementsFromCollection(validCards, numberToSelect);
            }
            default -> throw new GameProcessException(
                    "Error: CardHolders only support all/random selection");
        }
    }
}
