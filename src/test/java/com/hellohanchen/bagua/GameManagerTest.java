package com.hellohanchen.bagua;

import com.hellohanchen.bagua.board.Domain;
import com.hellohanchen.bagua.cardholders.PocketLibrary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GameManagerTest {
    private static GameManager gameManager;
    private static Player p1;
    private static Player p2;

    @BeforeAll
    public static void setup() {
        gameManager = new GameManager("p1", "p2");
        p1 = gameManager.getPlayer1();
        p2 = gameManager.getPlayer2();
    }


    @Test
    public void testSetup() {
        gameManager.process();

        PocketLibrary p1cards = gameManager.getCardHolder(p1);
        PocketLibrary p2cards = gameManager.getCardHolder(p2);

        gameManager.addPendingCard(p1, p1cards.getCards().get(0), 31);
        gameManager.process();
        gameManager.addPendingCard(p2, p2cards.getCards().get(0), 42);
        gameManager.process();
    }
}