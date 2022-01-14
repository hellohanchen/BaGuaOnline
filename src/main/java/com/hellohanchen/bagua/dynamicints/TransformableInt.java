package com.hellohanchen.bagua.dynamicints;

/**
 * TransformableInt is "DynamicIntWrapper" in the C# project.
 * DynamicIntWrapper wraps another DynamicInt as base int and can apply
 * transforms on the base int.
 */
public abstract class TransformableInt extends DynamicInt {
    protected final DynamicInt base;

    public TransformableInt(DynamicInt base) {
        super(base.getOriginalValue());
        this.base = base;
    }

    public TransformableInt(int baseValue) {
        this(new ModifiableInt(baseValue));
    }

    @Override
    public int getCurrentValue() {
        return base.getCurrentValue();
    }

    @Override
    public int getModifiableValue() {
        return base.getModifiableValue();
    }

    @Override
    public void setValue(int value) {
        base.setValue(value);
    }
}
