package com.hellohanchen.bagua.visitors;

import com.hellohanchen.bagua.effects.*;

public interface IEffectVisitor {
    default void visit(Effect effect) {
        effect.accept(this);
    }

    void visitAddCard(AddCard effect);
    void visitAttack(Attack attack);
    void visitBuffCharacter(BuffCharacter effect);
    void visitBuffSpellDamage(BuffSpellDamage effect);
    void visitCopyCard(CopyCard effect);
    void visitCopyCharacter(CopyCharacter effect);
    void visitDamage(Damage effect);
    void visitDeath(Death effect);
    void visitDestroy(Destroy effect);
    void visitDrawCard(DrawCard effect);
    void visitGuaLevelUp(GuaLevelUp effect);
    void visitHeal(Heal effect);
    void visitRemoveCard(RemoveCard effect);
    void visitSummon(Summon effect);
}
