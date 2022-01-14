package com.hellohanchen.bagua.guas;

import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.enums.Element;
import com.hellohanchen.bagua.statics.GameUtils;
import lombok.Getter;

public class SingularGua extends GuaSymbol {
    @Getter
    private final Element element;
    @Getter
    private final DynamicInt level;

    public SingularGua(Element element, DynamicInt level) {
        this.element = element;
        this.level = level;
    }

    @Override
    public int getLevelValue() {
        return level.getCurrentValue();
    }

    public void addLevel(int add) {
        this.level.add(add);
    }

    public void setLevel(int level) {
        this.level.setValue(level);
    }

    @Override
    public SingularGua getCopy() {
        return new SingularGua(element, level.getCopy());
    }

    public String getName() {
        return element.getName();
    }

    public CompoundGua combine(SingularGua other) {
        return new CompoundGua(this, other);
    }
}
