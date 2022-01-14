package com.hellohanchen.bagua.constraints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.visitors.IConstraintChecker;

public class AndConstraint extends ComplexConstraint {
    private final Constraint first;
    private final Constraint second;

    protected AndConstraint(Constraint first, Constraint second) {
        this.first = first;
        this.second = second;
    }

    public Constraint getFirst() {
        return first;
    }

    public Constraint getSecond() {
        return second;
    }

    @Override
    public void activate(GameObject activator, int effectId, int targetId) {
        first.activate(activator, effectId, targetId);
        second.activate(activator, effectId, targetId);
    }

    @Override
    public <T> T accept(IConstraintChecker<T> constraintChecker) {
        return constraintChecker.checkAndConstraint(this);
    }
}
