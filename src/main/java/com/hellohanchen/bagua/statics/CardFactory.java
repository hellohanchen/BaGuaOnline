package com.hellohanchen.bagua.statics;

import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.cards.GuaCard;
import com.hellohanchen.bagua.dynamicints.ModifiableInt;
import com.hellohanchen.bagua.enums.Element;
import com.hellohanchen.bagua.guas.GuaSymbol;
import com.hellohanchen.bagua.guas.SingularGua;

public class CardFactory {
    public static Card generateCard(int cardCode, int level) {
        if (cardCode < 8) {
            return new GuaCard(
                    new SingularGua(
                            Element.values()[cardCode],
                            new ModifiableInt(level)));
        }

        return null;
    }

    public static String generateCardDescription(GuaSymbol symbol) {
        StringBuilder description = new StringBuilder();

        if (symbol instanceof SingularGua singularGua) {
            switch (singularGua.getElement()) {
                case Kun -> description.append("召唤1名坤字护卫");
                case Gen -> description.append("召唤1名艮字护卫");
                case Kan -> description.append("为1名角色恢复")
                        .append(symbol.getLevelValue() * 2)
                        .append("点生命值");
                case Xun -> description.append("为1名护卫增加")
                        .append(symbol.getLevelValue())
                        .append("点攻击力，召唤1名巽字护卫");
                case Zhen -> description.append("为1名护卫减少")
                        .append(symbol.getLevelValue())
                        .append("点攻击力，召唤1名震字护卫");
                case Li -> description.append("对1名角色造成")
                        .append(symbol.getLevelValue())
                        .append("点伤害,召唤1名离字护卫");
                case Dui -> description.append("使随机1张‘卦’牌升")
                        .append(symbol.getLevelValue())
                        .append("级");
                case Qian ->description.append("召唤1名乾字护卫");
            }

            description.append(",并将手牌中的所有‘")
                    .append(singularGua.getName())
                    .append("’升1级");
        } else {
            // TODO: complete for compound gua
        }

        return description.toString();
    }
}
