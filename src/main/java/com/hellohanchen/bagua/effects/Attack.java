package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.characters.Character;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import com.hellohanchen.bagua.statics.TargetFactory;
import com.hellohanchen.bagua.visitors.IEffectVisitor;

/**
 * Attack is the effect represent one character attacking another one.
 */
public class Attack extends Effect {
    public final Character attacker;
    public final Character defender;

    public Attack(Character attacker, Character defender) {
        super("Attack", attacker + "攻击" + defender, TargetFactory.selectOccupiedCharacter());
        this.attacker = attacker;
        this.defender = defender;
    }

    @Override
    public Attack getCopy() {
        throw new GameProcessException("Error: Can't get copy of characterAttackEffect.");
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitAttack(this);
    }
}
