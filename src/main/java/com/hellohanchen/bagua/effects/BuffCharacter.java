package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.dynamicints.inttransform.IntTransform;
import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;

/**
 * Effects that modify character's attributes.
 */
public class BuffCharacter extends Buff {
    public final IntTransform attack;
    public final IntTransform shield;
    public final IntTransform health;

    public BuffCharacter(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint,
            IntTransform attack,
            IntTransform shield,
            IntTransform health
    ) {
        super(name, description, targetType, executionConstraint);
        this.attack = attack;
        this.shield = shield;
        this.health = health;
    }

    @Override
    public BuffCharacter getCopy() {
        return new BuffCharacter(
                name,
                description,
                getTargetType(),
                getExecutionConstraint(),
                attack.getCopy(),
                shield.getCopy(),
                health.getCopy());
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitBuffCharacter(this);
    }
}
