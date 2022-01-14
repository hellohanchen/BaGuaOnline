package com.hellohanchen.bagua.effects.record;

import com.hellohanchen.bagua.target.TargetPQ;
import com.hellohanchen.bagua.enums.EffectRecordType;

import java.util.Comparator;
import java.util.PriorityQueue;

import static com.hellohanchen.bagua.enums.EffectRecordType.*;

public class EffectRecordPQ extends PriorityQueue<EffectRecord> {
    private int counter = 0;

    public EffectRecordPQ() {
        super(getComparator());
    }

    /**
     * Determine the priority between two effects. Would use the following steps:
     *  1. effect with higher activation type would get higher priority
     *  2. effect with lower parentEventId would get higher priority
     *  3. effect with lower generatorObjectId would get higher priority
     *  4. effect with lower effectId would get higher priority
     * @return comparator used in this PQ
     */
    private static Comparator<EffectRecord> getComparator() {
        return (effectRecordA, effectRecordB) -> {
            int check = compare(effectRecordA.type(), effectRecordB.type());
            if (check != 0) return - check;
            if (effectRecordA.parentEventId() != effectRecordB.parentEventId()) {
                return effectRecordA.parentEventId() < effectRecordB.parentEventId() ? -1 : 1;
            }

            if (effectRecordA.activatorId() != effectRecordB.activatorId())
                return effectRecordA.activatorId() < effectRecordB.activatorId() ? -1 : 1;

            if (effectRecordA.effectId() != effectRecordB.effectId()) {
                return effectRecordA.effectId() < effectRecordB.effectId() ? -1 : 1;
            }

            return 0;
        };
    }

    private static int compare(EffectRecordType typeA, EffectRecordType typeB) {
        if (typeA == typeB) return 0;
        return switch (typeA) {
            case Death -> -1;
            case Destroy -> typeB == Death ? 1 : -1;
            case EventTriggered -> typeB == Death || typeB == Destroy ? 1 : -1;
            case DeathTriggered -> typeB == DeathTriggered || typeB == PlayerAction ? -1 : 1;
            default -> 1;
        };
    }

    /**
     * Remove the existence of a character that would be destroyed.
     * @param id character id
     */
    public void removeCharacter(int id) {
        this.removeIf(effectRecord -> {
            if (effectRecord.activatorId() == id && effectRecord.type() != EventTriggered) {
                return true;
            } else {
                TargetPQ targets = effectRecord.targets();
                if (targets != null && targets.stream().anyMatch(t -> t.getId() == id)) {
                    targets.removeId(id);
                    return targets.size() == 0;
                }
            }

            return false;
        });
    }

    /* Getters */
    public int nextId() {
        return ++counter;
    }

    public int lastId() {
        return counter;
    }

    public EffectRecord top() {
        return this.peek();
    }

    public EffectRecord pop() {
        return this.poll();
    }
}
