package com.hellohanchen.bagua.enums;

import lombok.Getter;

public enum Element {
    Kun("坤", "地"),
    Gen("艮", "山"),
    Kan("坎", "水"),
    Xun("巽", "风"),
    Zhen("震", "雷"),
    Li("离", "火"),
    Dui("兑", "泽"),
    Qian("乾", "天");

    @Getter
    private final String element;
    @Getter
    private final String symbol;

    Element(String element, String symbol) {
        this.element = element;
        this.symbol = symbol;
    }

    public String getName() {
        return element + symbol;
    }
}
