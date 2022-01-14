package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.enums.TargetOwnerLogic;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;
import lombok.Getter;

/**
 * CopyCharacterEffect can copy the currentSummon effect from a Character
 * and summon that effect at a random summonable Character position of the target owner.
 */
public class CopyCharacter extends Effect {
    @Getter
    private final TargetOwnerLogic toOwner;
    @Getter
    private final DynamicInt num;

    public CopyCharacter(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint,
            TargetOwnerLogic toOwner,
            DynamicInt num
    ) {
        super(name, description, targetType, executionConstraint);
        this.toOwner = toOwner;
        this.num = num;
    }

    @Override
    public CopyCharacter getCopy() {
        return new CopyCharacter(
                name, description, getTargetType(),
                getExecutionConstraint(), toOwner, num.getCopy()
        );
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitCopyCharacter(this);
    }
}
