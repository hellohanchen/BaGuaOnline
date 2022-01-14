package com.hellohanchen.bagua.dynamicints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.statics.GameUtils;

import java.util.ArrayList;

public class CharacterDamage extends TransformableInt {
    public CharacterDamage(DynamicInt base) {
        super(base instanceof CharacterDamage damage? damage.base : base);
    }

    public CharacterDamage(int baseValue) {
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
    public int getCurrentValue(GameObject character, GameObject target) {
        return character == null ?
                base.getCurrentValueUnsigned() :
                GameUtils.applyTransformChain(
                        base.getCopy(),
                        new ArrayList<>(),
                        character,
                        target
                ).getCurrentValueUnsigned(character, target);
    }

    @Override
    public CharacterDamage getCopy() {
        return new CharacterDamage(base.getCopy());
    }
}
