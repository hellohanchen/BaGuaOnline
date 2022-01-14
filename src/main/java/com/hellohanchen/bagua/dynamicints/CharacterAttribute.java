package com.hellohanchen.bagua.dynamicints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.characters.Character;

public abstract class CharacterAttribute extends GameStatus {
    public abstract int getAttribute(Character character);

    @Override
    public int getCurrentValue(GameObject activator) {
        if (!(activator instanceof Character)) {
            return -1;
        }

        return getAttribute((Character) activator);
    }
}
