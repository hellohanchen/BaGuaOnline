package com.hellohanchen.bagua.enums;

/**
 * Type of events that can activate an effect.
 */
public enum EffectRecordType {
    PlayerAction,
    EventTriggered,
    DeathTriggered,
    Death,
    Destroy,
    GameState,  // triggered by Game State Logic
    EffectGenerated
}
