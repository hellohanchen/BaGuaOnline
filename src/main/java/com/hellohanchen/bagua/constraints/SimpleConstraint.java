package com.hellohanchen.bagua.constraints;

import com.hellohanchen.bagua.visitors.IConstraintChecker;

import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantFalseConstraint.FALSE;
import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantTrueConstraint.TRUE;

/**
 * Simplest constant constraint represent TRUE or FALSE.
 */
public abstract class SimpleConstraint extends Constraint {
    public abstract SimpleConstraint Negate();

    public static class ConstantTrueConstraint extends SimpleConstraint {
        public static final SimpleConstraint TRUE = new ConstantTrueConstraint();

        public <T> T accept(IConstraintChecker<T> constraintChecker) {
            return constraintChecker.checkTrue();
        }

        @Override
        public SimpleConstraint Negate() {
            return FALSE;
        }

        @Override
        public Constraint and(Constraint other) {
            return other;
        }
    }

    public static class ConstantFalseConstraint extends SimpleConstraint {
        public static final SimpleConstraint FALSE = new ConstantFalseConstraint();

        public <T> T accept(IConstraintChecker<T> constraintChecker) {
            return constraintChecker.checkFalse();
        }

        @Override
        public SimpleConstraint Negate() {
            return TRUE;
        }

        @Override
        public Constraint and(Constraint other) {
            return this;
        }
    }
}
