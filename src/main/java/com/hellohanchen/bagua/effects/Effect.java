package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.common.CopyOptional;
import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.interfaces.ICopy;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;
import lombok.Getter;

import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantTrueConstraint.TRUE;

/**
 * Effect is the basic and only functionality unit in Gua game.
 * Every gameObject interacts with other objects through effects.
 * Any effect would influence the game status so that the game can be played.
 * This is the basic class for all kinds of effect.
 */
public abstract class Effect implements ICopy<Effect> {
    @Getter
    public final String name;
    @Getter
    public final String description;
    @Getter
    private TargetType targetType;
    @Getter
    private Constraint executionConstraint;
    private final CopyOptional<Effect> optional;

    protected Effect(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint
    ) {
        this.name = name;
        this.description = description;
        this.targetType = targetType;
        this.executionConstraint = executionConstraint;
        this.optional = CopyOptional.of(this);
    }

    protected Effect(String name, TargetType targetType) {
        this(name, "", targetType);
    }

    protected Effect(String name, String description, TargetType targetType) {
        this(name, description, targetType, TRUE);
    }

    protected Effect(String name, TargetType targetType, Constraint executionConstraint) {
        this(name, "", targetType, executionConstraint);
    }

    /* Getters */
    public abstract Effect getCopy();

    public CopyOptional<Effect> asOptional() {
        return optional;
    }

    /* Setters */
    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public void setExecutionConstraint(Constraint executionConstraint) {
        this.executionConstraint = executionConstraint;
    }

    public abstract void accept(IEffectVisitor visitor);
}
