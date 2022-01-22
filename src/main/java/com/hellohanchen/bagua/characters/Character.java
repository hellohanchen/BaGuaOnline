package com.hellohanchen.bagua.characters;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.board.Domain;
import com.hellohanchen.bagua.dynamicints.CharacterDamage;
import com.hellohanchen.bagua.dynamicints.CharacterHealth;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.effects.auras.Aura;
import com.hellohanchen.bagua.effects.auras.AuraHolder;
import com.hellohanchen.bagua.effects.auras.IAuraTarget;
import com.hellohanchen.bagua.enums.CharacterAbility;
import com.hellohanchen.bagua.enums.CharacterType;
import com.hellohanchen.bagua.interfaces.ICarrier;
import com.hellohanchen.baguaserver.entity.GameStatus;
import com.hellohanchen.baguaserver.entity.GameStatus.CharacterData;
import com.hellohanchen.baguaserver.entity.GameStatus.IntData;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Characters are on-board gameObjects. Player can use characters to make actions in each round.
 */
public abstract class Character extends GameObject implements IAuraTarget, ICarrier<Aura> {
    @Setter
    @Getter
    private GameManager gameManager;
    @Setter
    private Domain domain;

    /* Auras */
    @Getter
    private final AuraHolder effectiveAuras = new AuraHolder();

    @Override
    public void addEffectiveAura(int carrierId, Aura aura) {
        effectiveAuras.add(carrierId, aura);
    }

    @Override
    public void removeEffectiveAura(int carrierId) {
        effectiveAuras.remove(carrierId);
        while (getHealth().getCurrentValue() <= 0) {
            getHealth().add(1);
        }
    }

    /* Character Attributes */
    public void beDamaged(int d) {
        int validDamage = Math.max(d - getShieldValue(), 0);
        getHealth().subtract(validDamage);
    }

    public void beHealed(int h) {
        getHealth().setValue(Math.min(getMaxHealthValue(), getHealthValue() + h));
    }

    /* Target Selection */
    public void select() {
        this.gameManager.select(this);
    }

    /* Getters */
    public abstract String getName();

    public abstract String getDescription();

    public abstract int getCharacterCode();

    public abstract CharacterType getType();

    public abstract CharacterDamage getDamage();

    public int getDamageValue() {
        return getDamage().getCurrentValue(this);
    }

    public int getDamageValue(GameObject target) {
        return getDamage().getCurrentValue(this, target);
    }

    public IntData getDamageData() {
        int damage = getDamageValue();
        return new IntData(
                damage,
                damage > getDamage().getOriginalValue(),
                damage < getDamage().getOriginalValue());
    }

    public abstract CharacterHealth getHealth();

    public int getHealthValue() {
        return getHealth().getCurrentValue(this);
    }

    public abstract CharacterHealth getMaxHealth();

    public int getMaxHealthValue() {
        return getMaxHealth().getCurrentValue(this);
    }

    public boolean isDamaged() {
        return getHealthValue() < getMaxHealthValue();
    }

    public IntData getHealthData() {
        int health = getHealthValue();
        return new IntData(
                health,
                health > getHealth().getOriginalValue(),
                isDamaged());
    }

    public abstract DynamicInt getShield();

    public int getShieldValue() {
        return getShield().getCurrentValue(this);
    }

    public abstract HashSet<CharacterAbility> getAbilities();

    public boolean hasAbility(CharacterAbility ability) {
        return getAbilities().contains(ability);
    }

    public abstract Domain.Position getPosition();

    @Override
    public List<Aura> getInner() {
        return List.of();
    }

    public boolean hasAura() {
        return !getInner().isEmpty();
    }

    public List<Aura> getActivatedAuras() {
        return getInner().stream()
                .filter(a ->
                        gameManager.checkConstraint(
                                a.inner().getExecutionConstraint()))
                .toList();
    }

    @Override
    public Character asGameObject() {
        return this;
    }

    public CharacterData asData() {
        return new CharacterData(
                getPosition().ordinal(),
                getId(),
                getName(),
                getDescription(),
                hasAura(),
                getDamageData(),
                getShield().asData(),
                getHealthData());
    }

    /* Setters */
    public abstract void setDamage(CharacterDamage damage);

    public void setDamage(int damage) {
        getDamage().setValue(damage);
    }

    public abstract void setHealth(CharacterHealth health);

    public void setHealth(int health) {
        getHealth().setValue(health);
        getMaxHealth().setValue(health);
    }

    public abstract void setShield(DynamicInt shield);

    public void setShield(int shield) {
        getShield().setValue(shield);
    }

    public void addAbility(CharacterAbility ability) {
        getAbilities().add(ability);
    }

    public abstract void setAbilities(Collection<CharacterAbility> abilities);
}
