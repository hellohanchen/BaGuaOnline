package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.statics.TargetFactory;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;

/**
 * Death effect is actually a "to be destroyed" effect.
 * Activated when a character has health smaller or equals to 0.
 * When executing this effect, the character would be removed from all game logic.
 * All Death effect would be followed by a generated destroy effect
 * which would remove the object instance from the game.
 * Death event is an internal event which would not be broadcast to triggered other effects.
 */
public class Death extends Effect {
    /**
     * The default constructor doesn't need any attribute,
     * would create a death effect for a single character.
     */
    public Death() {
        super("Character Death", TargetFactory.selectOccupiedCharacter());
    }

    public Death(String name, TargetType targetType) {
        super(name, targetType);
    }

    public Death(String name, TargetType targetType, Constraint executionConstraint) {
        super(name, targetType, executionConstraint);
    }

    @Override
    public Death getCopy() {
        return new Death(name, getTargetType(), getExecutionConstraint());
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitDeath(this);
    }
}
