package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.ConstantInt;
import com.hellohanchen.bagua.enums.TargetSelectMethod;
import com.hellohanchen.bagua.predicates.cardpredicates.PositionPredicate;
import com.hellohanchen.bagua.target.CardTargetType;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;
import lombok.Getter;

import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantTrueConstraint.TRUE;

/**
 * Remove a card from a player's pocket.
 */
public class RemoveCard extends Effect {
    @Getter
    private final CardTargetType cardTargetType;

    public RemoveCard(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint,
            CardTargetType cardTargetType
    ) {
        super(name, description, targetType, executionConstraint);
        this.cardTargetType = cardTargetType;
    }

    public RemoveCard(
            String name,
            String description,
            TargetType targetType,
            int positionInPocket
    ) {
        this(name,description, targetType, TRUE, cardPositionTarget(positionInPocket));
    }

    private static CardTargetType cardPositionTarget(int pos) {
        return new CardTargetType(
                new PositionPredicate(new ConstantInt(pos)),
                TargetSelectMethod.Generate,
                new ConstantInt(1));
    }

    @Override
    public RemoveCard getCopy() {
        return new RemoveCard(
                name,
                description,
                getTargetType(),
                getExecutionConstraint(),
                cardTargetType.getCopy()
        );
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitRemoveCard(this);
    }
}
