package com.hellohanchen.bagua.constraints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.visitors.IConstraintChecker;

/**
 * BaseConstraint used as effect activation condition or ...
 */
public abstract class Constraint {
    protected GameObject activator;
    protected int effectId;
    protected int targetId;

    public void activate(GameObject activator, int effectId, int targetId) {
        this.activator = activator;
        this.effectId = effectId;
        this.targetId = targetId;
    }

    public void activate(GameObject activator) {
        this.activate(activator, 0, 0);
    }

    public GameObject getActivator() {
        return activator;
    }

    public int getEffectId() {
        return effectId;
    }

    public int getTargetId() {
        return targetId;
    }

    public abstract <T> T accept(IConstraintChecker<T> constraintChecker);

    public Constraint and(Constraint other) {
        return new AndConstraint(this, other);
    }
}
