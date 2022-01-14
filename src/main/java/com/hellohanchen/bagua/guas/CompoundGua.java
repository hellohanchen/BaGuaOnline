package com.hellohanchen.bagua.guas;

public class CompoundGua extends GuaSymbol {
    public SingularGua first;
    public SingularGua second;

    public CompoundGua(SingularGua first, SingularGua second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int getLevelValue() {
        return first.getLevelValue() + second.getLevelValue();
    }

    @Override
    public GuaSymbol getCopy() {
        return new CompoundGua(first.getCopy(), second.getCopy());
    }
}
