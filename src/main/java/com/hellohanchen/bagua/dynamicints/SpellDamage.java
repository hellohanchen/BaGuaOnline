package com.hellohanchen.bagua.dynamicints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.cardholders.PocketLibrary;
import com.hellohanchen.bagua.statics.GameUtils;

public class SpellDamage extends TransformableInt {
    public SpellDamage(DynamicInt base) {
        super(base);
    }

    public SpellDamage(int baseValue) {
        super(baseValue);
    }

    @Override
    public int getCurrentValue(GameObject activator, GameObject target) {
        return activator == null
                ? base.getCurrentValue()
                : GameUtils.applyTransformChain(
                        base.getCopy(),
                        activator.get(PocketLibrary.class).getSpellDamageTransforms(),
                        target).getCurrentValue(activator, target);
    }

    @Override
    public SpellDamage getCopy() {
        return new SpellDamage(base.getCopy());
    }
}
