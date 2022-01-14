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
 * HealEffect is a type of effect that heals one or more characters.
 * The real heal value of this effect can be modified by other effects.
 */
public class Heal extends Effect {
    @Getter @Setter
    private DynamicInt heal;

    public Heal(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint,
            DynamicInt heal
    ) {
        super(name, description, targetType, executionConstraint);
        this.heal = heal;
    }

    public Heal(
            String name,
            TargetType targetType,
            DynamicInt heal
    ) {
        this(name, "", targetType, TRUE, heal);
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitHeal(this);
    }

    /* Getters */
    public int getCurrentHeal(GameObject activator, Character target) {
        return this.getHeal().getCurrentValue(activator, target);
    }

    @Override
    public Heal getCopy() {
        return new Heal(
                name, description, getTargetType(), getExecutionConstraint(),
                this.getHeal().getCopy()
        );
    }
}
