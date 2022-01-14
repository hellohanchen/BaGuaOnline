package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.characters.Character;
import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;
import lombok.Getter;
import lombok.Setter;

import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantTrueConstraint.TRUE;

/**
 * DamageEffect is a type of effect that causing damage to one or more characters.
 * The real damage value of this effect can be modified by other effects.
 */
public class Damage extends Effect {
    @Getter @Setter
    private DynamicInt damage;

    public Damage(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint,
            DynamicInt damage
    ) {
        super(name, description, targetType, executionConstraint);
        this.damage = damage;
    }

    public Damage(
            String name,
            TargetType targetType,
            DynamicInt damage
    ) {
        this(name, "", targetType, TRUE, damage);
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitDamage(this);
    }

    /* Getters */
    public int getCurrentDamage(GameObject activator, Character target) {
        return getDamage().getCurrentValue(activator, target);
    }

    @Override
    public Damage getCopy() {
        return new Damage(
                name, description, getTargetType(), getExecutionConstraint(),
                getDamage().getCopy()
        );
    }
}
