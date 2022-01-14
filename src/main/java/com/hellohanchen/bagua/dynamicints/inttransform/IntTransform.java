package com.hellohanchen.bagua.dynamicints.inttransform;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.interfaces.ICopy;

/**
 * BaseIntTransform is the basic class for all dynamic int transforms.
 * A transform can modify the base value of dynamic ints.
 * But would not change the original value.
 */
public abstract class IntTransform implements ICopy<IntTransform> {
    protected final DynamicInt inner;

    protected IntTransform(DynamicInt inner) {
        this.inner = inner;
    }

    public abstract DynamicInt apply(DynamicInt origin);

    public DynamicInt apply(DynamicInt origin, GameObject activator) {
        return apply(origin);
    }

    public DynamicInt apply(DynamicInt origin, GameObject activator, GameObject target) {
        return apply(origin, activator);
    }

    @Override
    public abstract IntTransform getCopy();
}
