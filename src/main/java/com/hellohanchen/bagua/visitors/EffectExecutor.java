package com.hellohanchen.bagua.visitors;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.cardholders.CardHolder;
import com.hellohanchen.bagua.cards.GuaCard;
import com.hellohanchen.bagua.characters.Character;
import com.hellohanchen.bagua.characters.Guard;
import com.hellohanchen.bagua.dynamicints.CharacterDamage;
import com.hellohanchen.bagua.dynamicints.CharacterHealth;
import com.hellohanchen.bagua.effects.*;
import com.hellohanchen.bagua.enums.TargetClass;
import com.hellohanchen.bagua.enums.TargetSelectMethod;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import com.hellohanchen.bagua.statics.CardFactory;
import com.hellohanchen.bagua.target.TargetPQ;
import com.hellohanchen.bagua.target.TargetType;

import java.util.List;

public class EffectExecutor implements IEffectVisitor {
    private final GameManager gameManager;
    private TargetPQ targets;
    private GameObject activator;

    public EffectExecutor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void execute(Effect effect, TargetPQ targets, GameObject activator) {
        this.targets = targets;
        this.activator = activator;
        visit(effect);
    }

    @Override
    public void visitAddCard(AddCard effect) {
        targets.mapTo(CardHolder.class)
                .forEach(pocket -> gameManager.addCardToPocket(
                        pocket.getOwner(),
                        CardFactory.generateCard(
                                effect.cardCode,
                                effect.level.getCurrentValue(activator, pocket))));
    }

    @Override
    public void visitAttack(Attack attack) {
        Character attacker = attack.attacker;
        Character defender = attack.defender;

        int defenderDamage = attack.defender.getDamageValue(attacker);
        int attackerDamage = attack.attacker.getDamageValue(attack.defender);

        if (defenderDamage > 0) {
            gameManager.damage(attacker, defenderDamage);
        }
        if (attackerDamage > 0) {
            gameManager.damage(defender, attackerDamage);
        }
    }

    @Override
    public void visitBuffCharacter(BuffCharacter effect) {
        targets.mapTo(Character.class)
                .forEach(target -> {
                    target.setDamage(
                            new CharacterDamage(
                                    effect.attack.apply(
                                            target.getDamage(),
                                            activator,
                                            target)));
                    target.setHealth(
                            new CharacterHealth(
                                    effect.health.apply(
                                            target.getHealth(),
                                            activator,
                                            target)));
                    target.setShield(
                            effect.shield.apply(
                                    target.getShield(),
                                    activator,
                                    target));
                });
    }

    @Override
    public void visitBuffSpellDamage(BuffSpellDamage effect) {
        throw new GameProcessException("Can't execute spell damage effect without aura");
    }

    @Override
    public void visitCopyCard(CopyCard effect) {
        if (targets.size() > 1) {
            throw new GameProcessException("Error: Can't copy card from two different sources.");
        }

        CardHolder fromHolder = targets.get(0).get(CardHolder.class);
        List<? extends GameObject> toHolders = gameManager.generateEffectTarget(
                effect.getTo(),
                activator);

        fromHolder.getCards(effect.getCardTargetType())
                .forEach(card -> toHolders.forEach(holder ->
                        gameManager.addCardToPocket(holder.getOwner(), card.getCopy())));
    }

    @Override
    public void visitCopyCharacter(CopyCharacter effect) {
        targets.mapTo(Guard.class)
                .forEach(character ->
                        gameManager.generateEffectTarget(
                                new TargetType(
                                        effect.getToOwner(),
                                        TargetClass.AvailableCharacter,
                                        TargetSelectMethod.Random,
                                        effect.getNum().getCurrentValue(activator)
                                ),
                                activator)
                        .forEach(target -> gameManager.summon(
                                target.get(Guard.class),
                                character.getCurrentAttributes().getCopy())));
    }

    @Override
    public void visitDamage(Damage effect) {
        targets.mapTo(Character.class)
                .forEach(target -> {
                    int damage = effect.getCurrentDamage(activator, target);
                    if (damage > 0) {
                        gameManager.damage(target, damage);
                    }
                });
    }

    @Override
    public void visitDeath(Death effect) {
        targets.mapTo(Character.class).forEach(gameManager::death);
    }

    @Override
    public void visitDestroy(Destroy effect)
    {
        targets.mapTo(Character.class).forEach(gameManager::destroy);
    }

    @Override
    public void visitDrawCard(DrawCard effect) {
        targets.mapTo(CardHolder.class)
                .forEach(target ->
                        gameManager.drawCard(
                                target.getOwner(),
                                effect.num.getCurrentValue(activator, target)));
    }

    @Override
    public void visitGuaLevelUp(GuaLevelUp effect) {
        int up = effect.getUp().getCurrentValue(activator);

        targets.mapTo(CardHolder.class)
                .forEach(holder ->
                        holder.getCards(effect.getCardTargetType())
                                .forEach(card ->
                                        gameManager.upLevelCard((GuaCard) card, up)));
    }

    @Override
    public void visitHeal(Heal effect) {
        targets.mapTo(Character.class)
                .forEach(target -> {
                    if (target.isDamaged()) {
                        gameManager.heal(
                                target, effect.getCurrentHeal(activator, target));
                    }
                });
    }

    @Override
    public void visitRemoveCard(RemoveCard effect) {
        targets.mapTo(CardHolder.class)
                .forEach(pocket ->
                        pocket.getCards(effect.getCardTargetType()).forEach(
                                gameManager::removeCard));
    }

    @Override
    public void visitSummon(Summon effect) {
        targets.mapTo(Character.class)
                .forEach(target -> gameManager.summon(
                        target, effect.getCopy()));
    }
}
