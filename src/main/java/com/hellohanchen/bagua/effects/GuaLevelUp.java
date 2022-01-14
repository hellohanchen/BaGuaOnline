package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.ConstantInt;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.enums.Element;
import com.hellohanchen.bagua.enums.TargetSelectMethod;
import com.hellohanchen.bagua.predicates.cardpredicates.CardPredicate;
import com.hellohanchen.bagua.predicates.cardpredicates.ElementPredicate;
import com.hellohanchen.bagua.target.CardTargetType;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;
import lombok.Getter;

import java.util.List;

public class GuaLevelUp extends Effect {
    @Getter
    private final CardTargetType cardTargetType;
    @Getter
    private final DynamicInt up;

    public GuaLevelUp(
            TargetType targetType,
            Constraint executionConstraint,
            List<Element> elements,
            DynamicInt up
    ) {
        super("GuaLevelUp", targetType, executionConstraint);
        this.cardTargetType = new CardTargetType(
                new ElementPredicate(elements),
                TargetSelectMethod.All,
                new ConstantInt(0));
        this.up = up;
    }

    public GuaLevelUp(
            TargetType targetType,
            Constraint executionConstraint,
            CardPredicate cardPredicate,
            DynamicInt up
    ) {
        super("GuaLevelUp", targetType, executionConstraint);
        this.cardTargetType = new CardTargetType(
                cardPredicate,
                TargetSelectMethod.All,
                new ConstantInt(0));
        this.up = up;
    }

    public GuaLevelUp(
            TargetType targetType,
            Constraint executionConstraint,
            CardTargetType cardTargetType,
            DynamicInt up
    ) {
        super("GuaLevelUp", targetType, executionConstraint);
        this.cardTargetType = cardTargetType;
        this.up = up;
    }

    @Override
    public GuaLevelUp getCopy() {
        return new GuaLevelUp(
                getTargetType(),
                getExecutionConstraint(),
                cardTargetType.getCopy(),
                up.getCopy());
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitGuaLevelUp(this);
    }
}
