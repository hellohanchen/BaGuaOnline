package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;

import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantTrueConstraint.TRUE;

/**
 * AddCardEffect can generate a new card and add the new card to player's cardPocket.
 */
public class AddCard extends Effect {
    public final int cardCode;
    public final DynamicInt level;

    public AddCard(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint,
            int cardCode,
            DynamicInt level
    ) {
        super(name, description, targetType, executionConstraint);
        this.cardCode = cardCode;
        this.level = level;
    }

    public AddCard(
            String name,
            String description,
            TargetType targetType,
            int cardCode,
            DynamicInt level
    ) {
        this(name, description, targetType, TRUE, cardCode, level);
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitAddCard(this);
    }

    @Override
    public AddCard getCopy() {
        return new AddCard(
                name,
                description,
                getTargetType(),
                getExecutionConstraint(),
                cardCode,
                level.getCopy());
    }
}
