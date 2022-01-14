package com.hellohanchen.bagua.enums;

public enum TargetSelectMethod {
    Select(false, true), // player needs to select a character target
    Layer(false, true), // player needs to select a character target, the effect would also apply on other targets in the same layer
    Neighbour(false, true), // player needs to select a character target, the effect would also apply on neighbour characters
    Line(false, true), // player needs to select a character target, the effect would also apply on other targets in the same line
    Generate(true, false), // gameManager generate non-character target by OwnerType, AURA can't use this type
    Random(true, false), // gameManager randomly select targets [based on some constraints], AURA can't use this type
    All(true, false), // gameManager select all objects of a target class
    Other(true, false), // same as All but except for the activator object
    Self(false, false), // select the effect activator itself or previous effect target
    None(false, false); // there is not an object target in this case, so doesn't need any selection

    private final boolean generatedAtRuntime;
    private final boolean selectionNeeded;

    TargetSelectMethod(boolean g, boolean s) {
        this.generatedAtRuntime = g;
        this.selectionNeeded = s;
    }

    public boolean isGeneratedAtRuntime() {
        return generatedAtRuntime;
    }

    public boolean isSelectionNeeded() {
        return selectionNeeded;
    }
}
