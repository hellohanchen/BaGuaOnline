package com.hellohanchen.bagua.events;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.enums.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Game event records an event happens on a single gameObject.
 * This event should only have an id, a type,
 * zero or one parentEffect, zero or one target object, and some childEffects.
 */
public record Event(
        int id,
        EventType type,
        int effectId,
        Optional<GameObject> target,
        int targetId,
        List<Integer> childEffects) {

    public Event(
            int id,
            EventType type,
            int effectId,
            GameObject target,
            List<Integer> childEffects) {
        this(id, type, effectId, Optional.of(target), target.getId(), childEffects);
    }

    public Event(
            int id,
            EventType type,
            int effectId,
            List<Integer> childEffects) {
        this(id, type, effectId, Optional.empty(), -1, childEffects);
    }

    public Event(int id, EventType type, int effectId, GameObject target) {
        this(id, type, effectId, target, new ArrayList<>());
    }

    public Event(int id, EventType type, int effectId) {
        this(id, type, effectId, Optional.empty(), -1, new ArrayList<>());
    }
}
