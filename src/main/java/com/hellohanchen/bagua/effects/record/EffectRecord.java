package com.hellohanchen.bagua.effects.record;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.target.TargetPQ;
import com.hellohanchen.bagua.effects.Effect;
import com.hellohanchen.bagua.enums.EffectRecordType;

public record EffectRecord(
        Effect effect,
        EffectRecordType type,
        GameObject activator,
        int effectId,
        int parentEventId,
        int activatorId,
        TargetPQ targets) {
    public boolean isRuntimeTargeting() {
        return effect.getTargetType().isRuntimeTargeting();
    }
}
