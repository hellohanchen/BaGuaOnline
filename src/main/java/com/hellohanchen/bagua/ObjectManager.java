package com.hellohanchen.bagua;

import com.hellohanchen.bagua.board.Domain;
import com.hellohanchen.bagua.cardholders.CardHolder;
import com.hellohanchen.bagua.cardholders.PocketLibrary;
import com.hellohanchen.bagua.cards.Card;

import java.util.HashMap;

public class ObjectManager {
    private final GameManager gameManager;

    private final HashMap<Player, PocketLibrary> cardHolders = new HashMap<>();
    private final HashMap<Player, Domain> domains = new HashMap<>();

    public ObjectManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /* Object Management */
    public void addCardHolder(PocketLibrary cardHolder) {
        cardHolder.setGameManager(gameManager);
        cardHolders.put(cardHolder.getOwner(), cardHolder);
    }

    public void addDomain(Domain domain) {
        domain.setGameManager(gameManager);
        domains.put(domain.getOwner(), domain);
    }

    public void addCardToPocket(Player player, Card card) {
        cardHolders.get(player).addCardToPocket(card);
    }

    /* Getters */
    public PocketLibrary getCardHolder(Player player) {
        return cardHolders.get(player);
    }

    public Domain getDomain(Player player) {
        return domains.get(player);
    }
}
