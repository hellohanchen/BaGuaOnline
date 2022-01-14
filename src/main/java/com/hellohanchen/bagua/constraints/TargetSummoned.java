package com.hellohanchen.bagua.constraints;

import com.hellohanchen.bagua.visitors.IConstraintChecker;

/**
 * TargetSummonedConstraint is to check whether a summon-effect
 * successfully summon a character.
 * i.e. The character is not killed right after being summoned.
 */
public class TargetSummoned extends TargetConstraint {
    @Override
    public <T> T accept(IConstraintChecker<T> constraintChecker) {
        return constraintChecker.checkTargetSummoned(this);
    }
}
