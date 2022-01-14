package com.hellohanchen.bagua.enums;

/**
 * GameObject classes to be targeted
 */
public enum TargetClass {
    OccupiedCharacter(true),
    OccupiedNonPlayerCharacter(true),
    UnOccupiedCharacter(true),
    AvailableCharacter(true),
    SummonableCharacter(true),
    AttackableCharacter(true),
    CardHolder(false),
    CardLibrary(false),
    CardPocket(false),
    GameBoard(false),
    NonObject(false),
    Any(false);

    private final boolean targetingAtCharacter;

    TargetClass(boolean t) {
        this.targetingAtCharacter = t;
    }

    public boolean isTargetingAtCharacter() {
        return targetingAtCharacter;
    }
}
