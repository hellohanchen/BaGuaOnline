package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.dynamicints.inttransform.IntTransform;
import com.hellohanchen.bagua.visitors.IEffectVisitor;

import static com.hellohanchen.bagua.enums.TargetOwnerLogic.ALLY;
import static com.hellohanchen.bagua.statics.TargetFactory.cardPocketOf;

/**
 * SpellDamageBuff can increase the damage cost by spell cards.
 * It would be applied to the card pocket object.
 */
public class BuffSpellDamage extends Buff {
    public final IntTransform spellDamage;

    public BuffSpellDamage(
            String name,
            String description,
            Constraint executionConstraint,
            IntTransform spellDamage
    ) {
        super(name, description, cardPocketOf(ALLY), executionConstraint);
        this.spellDamage = spellDamage;
    }

    @Override
    public BuffSpellDamage getCopy() {
        return new BuffSpellDamage(
                name, description, getExecutionConstraint(), spellDamage.getCopy());
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitBuffSpellDamage(this);
    }
}
