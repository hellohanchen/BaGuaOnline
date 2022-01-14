package com.hellohanchen.bagua.constraints;

import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.enums.ValueComparator;
import com.hellohanchen.bagua.visitors.IConstraintChecker;
import lombok.AllArgsConstructor;

/**
 * IntComparisonConstraint compares the current values of two DynamicInts.
 */
@AllArgsConstructor
public class IntComparison extends Constraint {
    public final DynamicInt left;
    public final ValueComparator op;
    public final DynamicInt right;
    public final int offset;

    @Override
    public <T> T accept(IConstraintChecker<T> constraintChecker) {
        return constraintChecker.checkIntComparison(this);
    }
}
