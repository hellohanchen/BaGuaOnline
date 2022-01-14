package com.hellohanchen.bagua.visitors;

import com.hellohanchen.bagua.constraints.*;
import com.hellohanchen.bagua.effects.Effect;

public interface IConstraintChecker<T> {
    default T check(Constraint constraint) {
        return constraint.accept(this);
    }

    T checkTrue();
    T checkFalse();
    T checkAndConstraint(AndConstraint constraint);
    T checkIntComparison(IntComparison constraint);
    T checkTargetKilled(TargetKilled constraint);
    T checkTargetSummoned(TargetSummoned constraint);
}
