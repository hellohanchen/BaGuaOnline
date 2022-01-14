package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.enums.TargetClass;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import com.hellohanchen.bagua.target.TargetType;

public abstract class CardHolderEffect extends Effect {
    protected CardHolderEffect(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint) {
        super(name, description, targetType, executionConstraint);
        if (targetType.targetClass != TargetClass.CardHolder) {
            throw new GameProcessException("Error: CardHolderEffect target is not card holder");
        }
    }
}
