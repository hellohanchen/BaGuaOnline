package com.hellohanchen.bagua.dynamicints;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.interfaces.ICopy;
import java.lang.Math;

/**
 * DynamicInt is the class for all values in the game. The value of DynamicInt is evaluated
 * at the run time with an activator and a target, both of them should be AstrolabeMono objects.
 * All DynamicInts are NON-NEGATIVE.
 * The default DynamicInt is not modifiable but has the method to modify its value.
 */
public abstract class DynamicInt implements ICopy<DynamicInt> {
    protected final int originalValue;

    protected DynamicInt(int originalValue) {
        this.originalValue = Math.max(originalValue, 0);
    }

    @Override
    public abstract DynamicInt getCopy();

    /* getCurrentValue */

    /**
     * @return Resolve the dynamicInt value without provided activator or target.
     */
    public abstract int getCurrentValue();

    /**
     * @param activator Object that activate this dynamicInt.
     * @return Resolve the dynamicInt value with the activator/owner of this DynamicInt
     */
    public int getCurrentValue(GameObject activator) {
        return getCurrentValue();
    }

    /**
     * @param activator Object that activate this dynamicInt.
     * @param target Object that this dynamicInt would be applied on.
     * @return Resolve the dynamicInt value with activator and target
     */
    public int getCurrentValue(GameObject activator, GameObject target) {
        return getCurrentValue();
    }

    /* Unsigned getCurrentValue */
    public int getCurrentValueUnsigned() {
        return Math.max(getCurrentValue(), 0);
    }

    public int getCurrentValueUnsigned(GameObject activator) {
        return Math.max(getCurrentValue(activator), 0);
    }

    public int getCurrentValueUnsigned(GameObject activator, GameObject target) {
        return Math.max(getCurrentValue(activator, target), 0);
    }

    /* Math operations */
    public void subtract(int sub) {
        setValue(getModifiableValue() - sub);
    }

    public void add(int add) {
        setValue(getModifiableValue() + add);
    }

    public void multiply(int mul) {
        setValue(getModifiableValue() * mul);
    }

    /* Value status */
    public Boolean IsEnhanced() {
        return getCurrentValue() > originalValue;
    }

    public Boolean IsImpaired() {
        return getCurrentValue() < originalValue;
    }

    public Boolean IsEnhanced(GameObject activator) {
        return getCurrentValue(activator) > originalValue;
    }

    public Boolean IsImpaired(GameObject activator) {
        return getCurrentValue(activator) < originalValue;
    }

    /* Getters */
    public int getOriginalValue() {
        return originalValue;
    }

    public int getModifiableValue() {
        return getOriginalValue();
    }

    public DynamicInt getOriginalCopy() {
        DynamicInt copy = getCopy();
        copy.setValue(originalValue);
        return copy;
    }

    @Override
    public String toString() {
        return String.valueOf(getCurrentValue());
    }

    /* Setters */
    public void setValue(int value) { }
}
