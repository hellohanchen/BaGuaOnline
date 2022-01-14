package com.hellohanchen.bagua.characters;

import com.hellohanchen.bagua.board.Domain;
import com.hellohanchen.bagua.dynamicints.CharacterDamage;
import com.hellohanchen.bagua.dynamicints.CharacterHealth;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.effects.Summon;
import com.hellohanchen.bagua.effects.auras.Aura;
import com.hellohanchen.bagua.enums.CharacterAbility;
import com.hellohanchen.bagua.enums.CharacterType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Guards use inner {@link Summon} effect to represent the active character.
 * When the effect is null, the character is EMPTY, otherwise the
 * characterType is the characterType from the effect.
 * All modification happen on an active character is actually modifying the inner effect.
 * When the character dies, the inner summon effect would get cleaned up and
 * a copy of the original summon effect would be sent to the character graveyard.
 */
public class Guard extends Character {
    @Getter
    private Summon currentAttributes;
    @Getter
    private Summon originalAttributes;
    @Getter
    protected CharacterHealth maxHealth;
    @Getter @Setter
    private Domain.Position position;

    public Guard(Domain.Position position) {
        this.position = position;
    }

    public Guard(Summon effect, int id, Domain.Position position) {
        this.id = id;
        this.currentAttributes = effect;
        this.originalAttributes = effect.getOriginalCopy();
        this.maxHealth = effect.getHealth().getCopy();
        this.position = position;
    }

    @Override
    public String getName() {
        return currentAttributes.getName();
    }

    @Override
    public String getDescription() {
        return currentAttributes.getDescription();
    }

    @Override
    public int getCharacterCode() {
        return currentAttributes.characterCode;
    }

    @Override
    public CharacterType getType() {
        return currentAttributes.type;
    }

    @Override
    public CharacterDamage getDamage() {
        return currentAttributes.getDamage();
    }

    @Override
    public CharacterHealth getHealth() {
        return currentAttributes.getHealth();
    }

    @Override
    public DynamicInt getShield() {
        return currentAttributes.getShield();
    }

    @Override
    public HashSet<CharacterAbility> getAbilities() {
        return currentAttributes.getAbilities();
    }

    @Override
    public List<Aura> getInner() {
        return currentAttributes.getAuras();
    }

    @Override
    public void setDamage(CharacterDamage damage) {
        currentAttributes.setDamage(damage);
    }

    @Override
    public void setHealth(CharacterHealth health) {
        currentAttributes.setHealth(health);
    }

    @Override
    public void setShield(DynamicInt shield) {
        currentAttributes.setShield(shield);
    }

    @Override
    public void setAbilities(Collection<CharacterAbility> abilities) {
        currentAttributes.setAbilities(new HashSet<>(abilities));
    }
}
