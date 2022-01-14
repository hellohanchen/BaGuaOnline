package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;

import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantTrueConstraint.TRUE;

public class DrawCard extends CardHolderEffect {
    public final DynamicInt num;

    public DrawCard(
            String name,
            TargetType targetType,
            Constraint executionConstraint,
            DynamicInt num
    ) {
        super(name, "抽" + num + "张牌", targetType, executionConstraint);
        this.num = num;
    }

    public DrawCard(
            String name,
            TargetType targetType,
            DynamicInt num
    ) {
        this(name, targetType, TRUE, num);
    }

    @Override
    public DrawCard getCopy() {
        return new DrawCard(name, getTargetType(), getExecutionConstraint(), num.getCopy());
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitDrawCard(this);
    }
}
