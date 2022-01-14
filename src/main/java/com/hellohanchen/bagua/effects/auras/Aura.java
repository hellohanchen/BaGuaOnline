package com.hellohanchen.bagua.effects.auras;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.effects.Buff;
import com.hellohanchen.bagua.enums.TargetClass;
import com.hellohanchen.bagua.enums.TargetSelectMethod;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import com.hellohanchen.bagua.interfaces.ICopy;
import com.hellohanchen.bagua.target.TargetType;

/**
 * Aura is similar to effect, but Aura doesn't need to be activated.
 * Aura would only be carried by Character.
 * Aura can only be effective on occupied characters.
 * Aura would have one inner BuffEffect to show the effect of this Aura.
 * Aura would be removed once the carrier is dead.
 */
public record Aura(Buff inner) implements ICopy<Aura> {
    public Aura {
        TargetType targetType = inner.getTargetType();

        if (targetType.targetClass != TargetClass.OccupiedCharacter
                && targetType.targetClass != TargetClass.CardHolder
                || targetType.getTargetSelectMethod() == TargetSelectMethod.Random
                || targetType.getTargetSelectMethod() == TargetSelectMethod.Select) {
            throw new GameProcessException("Wrong target type for aura: " + targetType);
        }

    }

    public TargetType getTargetType() {
        return inner.getTargetType();
    }

    public Aura getCopy() {
        return new Aura(inner.getCopy());
    }

    /**
     * Activate the inner effect of this Aura to track game status.
     * @param activator aura activator
     */
    public void activate(GameObject activator) {
        inner.getExecutionConstraint().activate(activator);
    }
}
