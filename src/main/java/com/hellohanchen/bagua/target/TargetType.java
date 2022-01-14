package com.hellohanchen.bagua.target;

import com.hellohanchen.bagua.Player;
import com.hellohanchen.bagua.enums.TargetClass;
import com.hellohanchen.bagua.enums.TargetOwnerLogic;
import com.hellohanchen.bagua.enums.TargetSelectMethod;
import com.hellohanchen.bagua.predicates.objectPredicates.ObjectPredicate;

import java.util.Optional;

/**
 * TargetType class is a structure indicate how each effect determine its target.
 * It has three variables, which enable different targeting logic.
 */
public class TargetType {
    private TargetOwnerLogic targetOwnerLogic;
    public final TargetClass targetClass;
    private TargetSelectMethod targetSelectMethod;
    private int numberToSelect;  // TODO: update to DynamicInt
    public final Optional<ObjectPredicate> targetPredicateOpt;

    public TargetType(
            TargetOwnerLogic targetOwnerLogic,
            TargetClass targetClass,
            TargetSelectMethod targetSelectMethod,
            int numberToSelect,
            ObjectPredicate predicate
    ) {
        this.targetOwnerLogic = targetOwnerLogic;
        this.targetClass = targetClass;
        this.targetSelectMethod = targetSelectMethod;
        this.numberToSelect = numberToSelect;
        this.targetPredicateOpt = Optional.ofNullable(predicate);
    }

    public TargetType(
            TargetOwnerLogic targetOwnerLogic,
            TargetClass targetClass,
            TargetSelectMethod targetSelectMethod,
            int numberToSelect
    ) {
        this(targetOwnerLogic, targetClass, targetSelectMethod, numberToSelect, null);
    }

    public TargetType(
            TargetOwnerLogic targetOwnerLogic,
            TargetClass targetClass,
            TargetSelectMethod targetSelectMethod
    ) {
        this(targetOwnerLogic, targetClass, targetSelectMethod, 1);
    }

    /**
     * Find out the correct player to target at.
     * @param player a player
     * @return targeted player
     */
    public Player actualPlayerToTarget(Player player) {
        return player.applyTargetLogic(targetOwnerLogic);
    }

    /* Getters */
    public TargetOwnerLogic getTargetOwnerLogic() {
        return targetOwnerLogic;
    }

    public TargetSelectMethod getTargetSelectMethod() {
        return targetSelectMethod;
    }

    public int getNumberToSelect() {
        return numberToSelect;
    }

    public boolean isSelectionNeeded() {
        return getTargetSelectMethod().isSelectionNeeded();
    }

    public boolean isSelfSelected() {
        return getTargetSelectMethod() == TargetSelectMethod.Self;
    }

    public boolean isRuntimeTargeting() {
        return getTargetSelectMethod().isGeneratedAtRuntime();
    }

    public boolean isCharacterTargeting() {
        return targetClass.isTargetingAtCharacter();
    }

    public boolean isNonObjectTargeting() {
        return targetClass == TargetClass.NonObject;
    }

    @Override
    public String toString() {
        return "TargetType{" +
                "targetOwnerLogic=" + targetOwnerLogic +
                ", targetClass=" + targetClass +
                ", targetSelectMethod=" + targetSelectMethod +
                ", numberToSelect=" + numberToSelect +
                ", targetPredicateOpt=" + targetPredicateOpt +
                '}';
    }

    /* Setters */
    public void setTargetOwnerLogic(TargetOwnerLogic targetOwnerLogic) {
        this.targetOwnerLogic = targetOwnerLogic;
    }

    public void setTargetSelectMethod(TargetSelectMethod targetSelectMethod) {
        this.targetSelectMethod = targetSelectMethod;
    }

    public void setNumberToSelect(int numberToSelect) {
        this.numberToSelect = numberToSelect;
    }
}
