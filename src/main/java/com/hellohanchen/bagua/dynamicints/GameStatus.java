package com.hellohanchen.bagua.dynamicints;

/**
 * GameStatus current value comes from the game status directly,
 * there is no original value or base value.
 */
public abstract class GameStatus extends DynamicInt {
    protected GameStatus() {
        super(0);
    }

    @Override
    public int getCurrentValue() {
        return originalValue;
    }
}
