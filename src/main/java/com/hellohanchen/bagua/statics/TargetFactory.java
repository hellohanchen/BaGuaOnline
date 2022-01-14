package com.hellohanchen.bagua.statics;

import com.hellohanchen.bagua.dynamicints.ModifiableInt;
import com.hellohanchen.bagua.enums.CardType;
import com.hellohanchen.bagua.enums.TargetOwnerLogic;
import com.hellohanchen.bagua.predicates.cardpredicates.CardTypePredicate;
import com.hellohanchen.bagua.predicates.cardpredicates.ElementPredicate;
import com.hellohanchen.bagua.target.CardTargetType;
import com.hellohanchen.bagua.target.TargetType;

import java.util.Collections;

import static com.hellohanchen.bagua.enums.TargetClass.*;
import static com.hellohanchen.bagua.enums.TargetOwnerLogic.ANY;
import static com.hellohanchen.bagua.enums.TargetSelectMethod.*;

public class TargetFactory {
    public static TargetType selectOccupiedCharacter() {
        return new TargetType(ANY, OccupiedCharacter, Select);
    }

    public static TargetType selectNonPlayerOccupiedCharacter() {
        return new TargetType(ANY, OccupiedNonPlayerCharacter, Select);
    }

    public static TargetType selectAvailableCharacter(TargetOwnerLogic owner) {
        return new TargetType(owner, AvailableCharacter, Select);
    }

    public static TargetType cardPocketOf(TargetOwnerLogic owner) {
        return new TargetType(owner, CardHolder, Generate);
    }

    public static CardTargetType randomGuaInPocket(int num) {
        return new CardTargetType(
                new CardTypePredicate(Collections.singleton(CardType.Gua)),
                Random,
                new ModifiableInt(num));
    }
}
