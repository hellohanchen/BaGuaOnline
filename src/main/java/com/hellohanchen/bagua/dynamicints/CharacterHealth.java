package com.hellohanchen.bagua.dynamicints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.statics.GameUtils;

import java.util.ArrayList;

public class CharacterHealth extends TransformableInt {
    public CharacterHealth(DynamicInt base) {
        super(base instanceof CharacterHealth health? health.base : base);
    }

    public CharacterHealth(int baseValue) {
        super(baseValue);
    }

    @Override
    public int getCurrentValue(GameObject character) {
        return character == null ?
                base.getCurrentValueUnsigned() :
                GameUtils.applyTransformChain(
                        base.getCopy(),
                        new ArrayList<>(),
                        character
                ).getCurrentValueUnsigned(character);
    }

    @Override
    public CharacterHealth getCopy() {
        return new CharacterHealth(base.getCopy());
    }
}
