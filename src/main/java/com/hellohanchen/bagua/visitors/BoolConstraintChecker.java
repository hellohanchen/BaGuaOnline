package com.hellohanchen.bagua.visitors;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.constraints.AndConstraint;
import com.hellohanchen.bagua.constraints.IntComparison;
import com.hellohanchen.bagua.constraints.TargetKilled;
import com.hellohanchen.bagua.constraints.TargetSummoned;
import com.hellohanchen.bagua.exceptions.GameProcessException;

public class BoolConstraintChecker implements IConstraintChecker<Boolean> {
    private final GameManager gameManager;

    public BoolConstraintChecker(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public Boolean checkTrue() {
        return true;
    }

    @Override
    public Boolean checkFalse() {
        return false;
    }

    @Override
    public Boolean checkAndConstraint(AndConstraint constraint) {
        return check(constraint.getFirst()) && check(constraint.getSecond());
    }

    @Override
    public Boolean checkIntComparison(IntComparison constraint) {
        return switch (constraint.op) {
            case Equals ->
                    constraint.left.getCurrentValue(constraint.getActivator()) ==
                            constraint.right.getCurrentValue(constraint.getActivator())
                                    + constraint.offset;
            case LessEqual ->
                    constraint.left.getCurrentValue(constraint.getActivator()) <=
                            constraint.right.getCurrentValue(constraint.getActivator())
                                    + constraint.offset;
            case LessThan ->
                    constraint.left.getCurrentValue(constraint.getActivator()) <
                            constraint.right.getCurrentValue(constraint.getActivator())
                                    + constraint.offset;
            case GreaterEqual ->
                    constraint.left.getCurrentValue(constraint.getActivator()) >=
                            constraint.right.getCurrentValue(constraint.getActivator())
                                    + constraint.offset;
            case GreaterThan ->
                    constraint.left.getCurrentValue(constraint.getActivator()) >
                            constraint.right.getCurrentValue(constraint.getActivator())
                                    + constraint.offset;
        };
    }

    @Override
    public Boolean checkTargetKilled(TargetKilled constraint) {
        if (constraint.getEffectId() == 0 || constraint.getTargetId() == 0) {
            throw new GameProcessException("ConstraintChecker: Constraint is not activated.");
        }

        return gameManager.targetKilledByEffect(
                constraint.getEffectId(),
                constraint.getTargetId());
    }

    @Override
    public Boolean checkTargetSummoned(TargetSummoned constraint) {
        if (constraint.getEffectId() == 0 || constraint.getTargetId() == 0) {
            throw new GameProcessException("ConstraintChecker: Constraint is not activated.");
        }

        return gameManager.getObject(
                gameManager.getCharacterIdSummonedByEffect(constraint.getEffectId())) != null;
    }
}
