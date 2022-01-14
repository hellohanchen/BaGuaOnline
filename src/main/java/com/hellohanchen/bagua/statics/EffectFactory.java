package com.hellohanchen.bagua.statics;

import com.hellohanchen.bagua.dynamicints.*;
import com.hellohanchen.bagua.dynamicints.inttransform.MinusTransform;
import com.hellohanchen.bagua.dynamicints.inttransform.PlusTransform;
import com.hellohanchen.bagua.effects.*;
import com.hellohanchen.bagua.guas.GuaSymbol;
import com.hellohanchen.bagua.guas.SingularGua;

import java.util.ArrayList;
import java.util.List;

import static com.hellohanchen.bagua.constraints.SimpleConstraint.ConstantTrueConstraint.TRUE;
import static com.hellohanchen.bagua.enums.TargetOwnerLogic.ALLY;

public class EffectFactory {
    // TODO: Consider Aura/Activator/Target
    public static List<Effect> generateEffects(GuaSymbol symbol) {
        List<Effect> effects = new ArrayList<>();

        if (symbol instanceof SingularGua singularGua) {
            switch (singularGua.getElement()) {
                case Kun, Gen, Qian -> effects.add(generateSummon(singularGua));
                case Kan -> effects.add(new Heal(
                        singularGua.getName(),
                        TargetFactory.selectOccupiedCharacter(),
                        new ModifiableInt(singularGua.getLevelValue() * 2)));
                case Xun -> {
                    effects.add(new BuffCharacter(
                            singularGua.getName(),
                            "",
                            TargetFactory.selectNonPlayerOccupiedCharacter(),
                            TRUE,
                            new PlusTransform(new ConstantInt(singularGua.getLevelValue())),
                            new PlusTransform(new ConstantInt(0)),
                            new PlusTransform(new ConstantInt(0))));
                    effects.add(generateSummon(singularGua));
                }
                case Zhen -> {
                    effects.add(new BuffCharacter(
                            singularGua.getName(),
                            "",
                            TargetFactory.selectNonPlayerOccupiedCharacter(),
                            TRUE,
                            new MinusTransform(new ConstantInt(singularGua.getLevelValue())),
                            new PlusTransform(new ConstantInt(0)),
                            new PlusTransform(new ConstantInt(0))));
                    effects.add(generateSummon(singularGua));
                }
                case Li -> {
                    effects.add(new Damage(
                            singularGua.getName(),
                            TargetFactory.selectOccupiedCharacter(),
                            new SpellDamage(singularGua.getLevelValue() * 2)));
                    effects.add(generateSummon(singularGua));
                }
                case Dui -> effects.add(new GuaLevelUp(
                        TargetFactory.cardPocketOf(ALLY),
                        TRUE,
                        TargetFactory.randomGuaInPocket(1),
                        new ModifiableInt(singularGua.getLevelValue())));
            }
        }

        return effects;
    }

    public static Summon generateSummon(SingularGua gua) {
        return switch (gua.getElement()) {
            case Kun -> Summon.builder()
                    .characterCode(1000)
                    .name("坤字护卫")
                    .description("")
                    .targetType(TargetFactory.selectAvailableCharacter(ALLY))
                    .damage(new CharacterDamage(Math.max(gua.getLevelValue() - 1, 1)))
                    .health(new CharacterHealth(gua.getLevelValue() + 1))
                    .shield(new ModifiableInt(0))
                    .build();
            case Gen -> Summon.builder()
                    .characterCode(1001)
                    .name("艮字护卫")
                    .description("")
                    .targetType(TargetFactory.selectAvailableCharacter(ALLY))
                    .damage(new CharacterDamage(Math.max(gua.getLevelValue() - 1, 1)))
                    .health(new CharacterHealth(gua.getLevelValue()))
                    .shield(new ModifiableInt(1))
                    .build();
            case Xun -> Summon.builder()
                    .characterCode(1003)
                    .name("巽字护卫")
                    .description("")
                    .targetType(TargetFactory.selectAvailableCharacter(ALLY))
                    .damage(new CharacterDamage(1))
                    .health(new CharacterHealth(gua.getLevelValue()))
                    .shield(new ModifiableInt(0))
                    .build();
            case Zhen -> Summon.builder()
                    .characterCode(1004)
                    .name("震字护卫")
                    .description("")
                    .targetType(TargetFactory.selectAvailableCharacter(ALLY))
                    .damage(new CharacterDamage(1))
                    .health(new CharacterHealth(gua.getLevelValue()))
                    .shield(new ModifiableInt(0))
                    .build();
            case Li -> Summon.builder()
                    .characterCode(1005)
                    .name("离字护卫")
                    .description("")
                    .targetType(TargetFactory.selectAvailableCharacter(ALLY))
                    .damage(new CharacterDamage(gua.getLevelValue()))
                    .health(new CharacterHealth(1))
                    .shield(new ModifiableInt(0))
                    .build();
            case Qian -> Summon.builder()
                    .characterCode(1007)
                    .name("乾字护卫")
                    .description("")
                    .targetType(TargetFactory.selectAvailableCharacter(ALLY))
                    .damage(new CharacterDamage(gua.getLevelValue() + 1))
                    .health(new CharacterHealth(Math.max(gua.getLevelValue() - 1, 1)))
                    .shield(new ModifiableInt(0))
                    .build();
            default -> null;
        };
    }
}
