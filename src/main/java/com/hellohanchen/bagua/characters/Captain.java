package com.hellohanchen.bagua.characters;

import com.hellohanchen.bagua.Player;
import com.hellohanchen.bagua.board.Domain;
import com.hellohanchen.bagua.dynamicints.CharacterDamage;
import com.hellohanchen.bagua.dynamicints.CharacterHealth;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.dynamicints.ModifiableInt;
import com.hellohanchen.bagua.enums.CharacterAbility;
import com.hellohanchen.bagua.enums.CharacterType;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

public class Captain extends Character {
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter @Setter
    private CharacterDamage damage;
    @Getter @Setter
    private CharacterHealth health;
    @Getter
    private final CharacterHealth maxHealth;
    @Getter @Setter
    private DynamicInt shield;
    @Getter
    private HashSet<CharacterAbility> abilities;

    public Captain(Player p, int id) {
        this.owner = p;
        this.id = id;
        this.shield = new ModifiableInt(0);
        this.health = new CharacterHealth(new ModifiableInt(10));
        this.maxHealth = this.health.getCopy();
    }

    @Override
    public int getCharacterCode() {
        return 0;
    }

    @Override
    public CharacterType getType() {
        return CharacterType.CAPTAIN;
    }

    @Override
    public Domain.Position getPosition() {
        return Domain.Position.Captain;
    }

    @Override
    public void setAbilities(Collection<CharacterAbility> abilities) {
        this.abilities = new HashSet<>(abilities);
    }
}
