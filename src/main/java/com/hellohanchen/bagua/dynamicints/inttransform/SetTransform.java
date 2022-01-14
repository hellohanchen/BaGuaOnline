package com.hellohanchen.bagua.dynamicints.inttransform;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.dynamicints.DynamicInt;

public class SetTransform extends IntTransform {
    public SetTransform(DynamicInt inner) {
        super(inner);
    }

    @Override
    public DynamicInt apply(DynamicInt origin) {
        origin.setValue(inner.getCurrentValue());
        return origin;
    }

    @Override
    public DynamicInt apply(DynamicInt origin, GameObject activator) {
        origin.setValue(inner.getCurrentValue(activator));
        return origin;
    }

    @Override
    public DynamicInt apply(DynamicInt origin, GameObject activator, GameObject target) {
        origin.setValue(inner.getCurrentValue(activator, target));
        return origin;
    }

    @Override
    public IntTransform getCopy() {
        return new SetTransform(inner.getCopy());
    }
}
