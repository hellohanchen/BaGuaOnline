package com.hellohanchen.bagua.dynamicints;

/**
 * ConstantInt is a constant value in the game. Would not change based on game status and can't be modified.
 */
public class ConstantInt extends DynamicInt {
    public ConstantInt(int originalValue) {
        super(originalValue);
    }

    @Override
    public int getCurrentValue() {
        return originalValue;
    }

    @Override
    public DynamicInt getCopy() {
        return new ConstantInt(originalValue);
    }
}
