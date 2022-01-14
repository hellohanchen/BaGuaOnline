package com.hellohanchen.bagua.dynamicints;

/**
 * ModifiableInt is "BaseInt" in C# project.
 * BaseInt represents normal integer values in the game, which would not
 * change with the game status.
 * BaseInt is modifiable.
 */
public class ModifiableInt extends DynamicInt {
    private int baseValue;

    public ModifiableInt(int originalValue, int baseValue) {
        super(originalValue);
        this.baseValue = baseValue;
    }

    public ModifiableInt(int originalValue) {
        this(originalValue, originalValue);
    }

    @Override
    public DynamicInt getCopy() {
        return new ModifiableInt(originalValue, baseValue);
    }

    @Override
    public int getCurrentValue() {
        return getModifiableValue();
    }

    @Override
    public int getModifiableValue() {
        return baseValue;
    }

    @Override
    public void setValue(int value) {
        this.baseValue = value;
    }
}
