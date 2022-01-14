package com.hellohanchen.bagua.guas;

import com.hellohanchen.bagua.interfaces.ICopy;

public abstract class GuaSymbol implements ICopy<GuaSymbol> {
    public abstract int getLevelValue();
    public abstract GuaSymbol getCopy();
}
