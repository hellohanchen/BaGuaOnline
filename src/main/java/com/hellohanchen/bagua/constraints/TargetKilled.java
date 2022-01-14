package com.hellohanchen.bagua.constraints;

import com.hellohanchen.bagua.visitors.IConstraintChecker;

/**
 * TargetedKilledConstraint would be TRUE if the targeted is killed by the registered effect.
 */
public class TargetKilled extends TargetConstraint {
    @Override
    public <T> T accept(IConstraintChecker<T> constraintChecker) {
        return constraintChecker.checkTargetKilled(this);
    }
}
