package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.CharacterDamage;
import com.hellohanchen.bagua.dynamicints.CharacterHealth;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.effects.auras.Aura;
import com.hellohanchen.bagua.enums.CharacterAbility;
import com.hellohanchen.bagua.enums.CharacterType;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.IEffectVisitor;
import lombok.*;

import java.util.HashSet;
import java.util.List;

/**
 * Summon effect can summon one assist character at the target position.
 * Some attributes such as damage and health can be modified by other effects.
 */
public class Summon extends Effect {
    public final int characterCode;
    public final CharacterType type;
    @Getter @Setter
    private CharacterDamage damage;
    @Getter @Setter
    private CharacterHealth health;
    @Getter @Setter
    private DynamicInt shield;
    @Getter @Singular
    private final List<Aura> auras;
    @Getter @Setter
    private HashSet<CharacterAbility> abilities;

    @Builder
    public Summon(
            String name,
            String description,
            TargetType targetType,
            Constraint executionConstraint,
            int characterCode,
            CharacterType type,
            CharacterDamage damage,
            CharacterHealth health,
            DynamicInt shield,
            List<Aura> auras,
            HashSet<CharacterAbility> abilities
    ) {
        super(name, description, targetType, executionConstraint);
        this.characterCode = characterCode;
        this.type = type;
        this.damage = damage;
        this.health = health;
        this.shield = shield;
        this.auras = auras;
        this.abilities = abilities == null ? new HashSet<>() : abilities;
    }

    /* Setters */
    public void addAura(Aura aura) {
        this.auras.add(aura);
    }

    public void addAbility(CharacterAbility ability) {
        this.abilities.add(ability);
    }

    @Override
    public Summon getCopy() {
        return new Summon(
                name,
                description,
                getTargetType(),
                getExecutionConstraint(),
                characterCode,
                type,
                damage.getCopy(),
                health.getCopy(),
                shield.getCopy(),
                auras.stream().map(Aura::getCopy).toList(),
                new HashSet<>(abilities)
        );
    }

    public Summon getOriginalCopy() {
        return new Summon(
                name,
                description,
                getTargetType(),
                getExecutionConstraint(),
                characterCode,
                type,
                (CharacterDamage) damage.getOriginalCopy(),
                (CharacterHealth) health.getOriginalCopy(),
                shield.getCopy(),
                auras.stream().map(Aura::getCopy).toList(),
                new HashSet<>(abilities)
        );
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitSummon(this);
    }
}
