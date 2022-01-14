package com.hellohanchen.bagua.dynamicints.inttransform;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.dynamicints.DynamicInt;

public class MinusTransform extends IntTransform {
    public MinusTransform(DynamicInt inner) {
        super(inner);
    }

    @Override
    public DynamicInt apply(DynamicInt origin) {
        origin.subtract(inner.getCurrentValue());
        return origin;
    }

    @Override
    public DynamicInt apply(DynamicInt origin, GameObject activator) {
        origin.subtract(inner.getCurrentValue(activator));
        return origin;
    }

    @Override
    public DynamicInt apply(DynamicInt origin, GameObject activator, GameObject target) {
        origin.subtract(inner.getCurrentValue(activator, target));
        return origin;
    }

    @Override
    public IntTransform getCopy() {
        return new MinusTransform(inner.getCopy());
    }
}
