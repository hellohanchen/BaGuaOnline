package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.target.TargetType;

/**
 * BuffEffect would modify characters or spell damages.
 * BuffEffect can increase/decrease character's attribute values
 * or give characters new ability or effect.
 */
public abstract class Buff extends Effect {
    protected Buff(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint) {
        super(name, description, targetType, executionConstraint);
    }

    @Override
    public abstract Buff getCopy();
}
