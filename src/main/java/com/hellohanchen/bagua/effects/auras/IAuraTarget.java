package com.hellohanchen.bagua.effects.auras;

import com.hellohanchen.bagua.GameManager;

/**
 * IAuraTarget is the interface for all game objects that can be influenced by Aura.
 * Most common cases are characters and cards.
 */
public interface IAuraTarget {
    /**
     * Set the gameManager instance to check the constraints of aura effects.
     * @param gameManager GameManager instance of this game
     */
    void setGameManager(GameManager gameManager);

    /**
     * Add a new effective aura with carrierId.
     * @param id id of aura carrier
     * @param aura aura effect to be added
     */
    void addEffectiveAura(int id, Aura aura);

    /**
     * Remove aura effect based on the carrierId.
     * @param id id of aura carrier
     */
    void removeEffectiveAura(int id);
}
