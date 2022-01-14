package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.target.CardTargetType;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;
import lombok.Getter;

/**
 * CopyCardEffect can make card copies from fromSource to toSource.
 * The cards are selected based on a cardTargetType object.
 */
public class CopyCard extends Effect {
    private final TargetType toTarget;
    @Getter
    private final CardTargetType cardTargetType;

    public CopyCard(
            String name,
            String description,
            TargetType fromTarget,
            Constraint executionConstraint,
            TargetType toTarget,
            CardTargetType cardTargetType
    ) {
        super(name, description, fromTarget, executionConstraint);
        this.toTarget = toTarget;
        this.cardTargetType = cardTargetType;
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitCopyCard(this);
    }

    /* Getters */
    public TargetType getFrom() {
        return getTargetType();
    }

    public TargetType getTo() {
        return toTarget;
    }

    @Override
    public CopyCard getCopy() {
        return new CopyCard(
                name,
                description,
                getFrom(),
                getExecutionConstraint(),
                getTo(),
                getCardTargetType()
        );
    }
}
