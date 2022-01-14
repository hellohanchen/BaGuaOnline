package com.hellohanchen.bagua.dynamicints;

import com.hellohanchen.bagua.characters.Character;

public class TargetCharacterMaxHealth extends TargetCharacterAttribute {
    @Override
    public int getAttribute(Character character) {
        return character.getMaxHealthValue();
    }

    @Override
    public DynamicInt getCopy() {
        return new TargetCharacterMaxHealth();
    }
}
