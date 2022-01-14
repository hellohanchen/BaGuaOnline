package com.hellohanchen.bagua.enums;

/**
 * Types of game events that would happen in Astrolabe game.
 */
public enum EventType {
    Damage,
    Death,
    Destroy,
    DrawCard,
    GameStart,
    Heal,
    Summon,
    UseCard,
    UseAbility,
    RoundStart,
    RoundEnd,
    Attack,
    Equip,
    EffectGeneration, // this type of event is for gameManager to generate new effects that would be activated later
    Move
}
