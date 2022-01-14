package com.hellohanchen.bagua.dynamicints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.characters.Character;

public abstract class TargetCharacterAttribute extends CharacterAttribute {
    @Override
    public int getCurrentValue(GameObject activator, GameObject target) {
        if (!(target instanceof Character)) {
            return -1;
        }

        return getAttribute((Character) target);
    }
}
