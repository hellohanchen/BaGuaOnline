package com.hellohanchen.bagua.constraints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.exceptions.GameObjectException;

import static com.hellohanchen.bagua.ConfigConstants.nullEffectId;

/**
 * EffectTargetConstraint is the kind of constraint that check status of effect's target.
 */
public abstract class TargetConstraint extends Constraint {
    @Override
    public void activate(GameObject activator, int effectId, int targetId) {
        if (effectId == nullEffectId) {
            throw new GameObjectException(
                    "EffectTargetConstraint requires a valid effect id.");
        }

        super.activate(activator, effectId, targetId);
    }
}
