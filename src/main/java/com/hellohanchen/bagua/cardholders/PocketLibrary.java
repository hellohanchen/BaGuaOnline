package com.hellohanchen.bagua.cardholders;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.Player;
import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.dynamicints.inttransform.IntTransform;
import com.hellohanchen.bagua.effects.auras.Aura;
import com.hellohanchen.bagua.effects.auras.AuraHolder;
import com.hellohanchen.bagua.enums.EventType;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import com.hellohanchen.bagua.statics.GameUtils;
import com.hellohanchen.baguaserver.entity.GameEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * CardHolder manage the card pocket and card library for each player.
 * Player can use cards in pocket directly. And cards in library must be
 * drawn to pocket before being used.
 */
public class PocketLibrary extends CardHolder {
    // functionally variables
    private final List<Card> cardPocket = new ArrayList<>();
    private final List<Card> cardLibrary = new ArrayList<>();

    // Auras
    private final AuraHolder effectiveAuras = new AuraHolder();

    public PocketLibrary(Player owner, int id) {
        this.owner = owner;
        this.id = id;
    }

    /* Card Pocket Management */
    @Override
    public void addCard(Card card) {
        addCardToLibrary(card);
    }

    public void addCardToPocket(Card card) {
        card.activate(gameManager, owner, pocketCount() + 1);
        cardPocket.add(card);
        gameManager.addNewEvent(EventType.AddCard, this, card);
    }

    public void removeCardAt(int pos) {
        if (pos > cardPocket.size()) {
            throw new GameProcessException("CardPocket: use card index out of bound.");
        }

        cardPocket.remove(pos - 1);
        updateCardPosition(pos - 1);
    }

    /* Card Library Management */
    public void addCardToLibrary(Card card) {
        cardLibrary.add(card);
    }

    public void addCardToLibraryRandomly(Card card) {
        cardLibrary.add(GameUtils.randomInt(0, cardLibrary.size()), card);
    }

    public Card popCardFromLibraryRandomly() {
        int index = GameUtils.randomInt(0, cardLibrary.size() - 1);
        Card card = cardLibrary.get(index);
        cardLibrary.remove(index);

        return card;
    }

    /* Effect */
    @Override
    public void addEffectiveAura(int carrierId, Aura aura) {
        effectiveAuras.add(carrierId, aura);
    }

    @Override
    public void removeEffectiveAura(int carrierId) {
        effectiveAuras.remove(carrierId);
    }

    /* Getters */
    public int pocketCount() {
        return cardPocket.size();
    }

    @Override
    public List<Card> getCards() {
        return cardPocket;
    }

    public boolean isLibraryEmpty() {
        return cardLibrary.isEmpty();
    }

    public int libraryCount() {
        return cardLibrary.size();
    }

    public List<IntTransform> getSpellDamageTransforms() {
        return effectiveAuras.getSpellDamageTransforms();
    }

    @Override
    public String toString() {
        return getOwner().toString() + "'s card holder-" + getId();
    }

    /* Setters */
    @Override
    public void setGameManager(GameManager gameManager) {
        super.setGameManager(gameManager);
        effectiveAuras.setGameManager(gameManager);
    }

    /* Utils */
    private void updateCardPosition(int start) {
        for (int i = start; i < cardPocket.size(); i++) {
            cardPocket.get(i).setPositionInPocket(i + 1);
        }
    }
}
