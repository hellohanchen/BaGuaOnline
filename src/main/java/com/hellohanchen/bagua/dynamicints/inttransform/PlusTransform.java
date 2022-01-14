package com.hellohanchen.bagua.dynamicints.inttransform;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.dynamicints.DynamicInt;

public class PlusTransform extends IntTransform {
    public PlusTransform(DynamicInt inner) {
        super(inner);
    }

    @Override
    public DynamicInt apply(DynamicInt origin) {
        origin.add(inner.getCurrentValue());
        return origin;
    }

    @Override
    public DynamicInt apply(DynamicInt origin, GameObject activator) {
        origin.add(inner.getCurrentValue(activator));
        return origin;
    }

    @Override
    public DynamicInt apply(DynamicInt origin, GameObject activator, GameObject target) {
        origin.add(inner.getCurrentValue(activator, target));
        return origin;
    }

    @Override
    public IntTransform getCopy() {
        return new PlusTransform(inner.getCopy());
    }
}
