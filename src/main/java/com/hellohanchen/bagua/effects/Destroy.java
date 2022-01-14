package com.hellohanchen.bagua.effects;

import com.hellohanchen.bagua.statics.TargetFactory;
import com.hellohanchen.bagua.visitors.IEffectVisitor;

/**
 * DestroyEffect would remove the character object instance from the game.
 */
public class Destroy extends Effect {
    /**
     * The default constructor doesn't need any attribute,
     * would create a destroyEffect for a single character.
     */
    public Destroy() {
        super("Character Death", TargetFactory.selectOccupiedCharacter());
    }

    @Override
    public Destroy getCopy() {
        return new Destroy();
    }

    @Override
    public void accept(IEffectVisitor visitor) {
        visitor.visitDestroy(this);
    }
}
