package com.hellohanchen.bagua.statics;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.dynamicints.DynamicInt;
import com.hellohanchen.bagua.dynamicints.inttransform.IntTransform;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GameUtils {
    public static int randomInt(int lower, int upper) {
        return ThreadLocalRandom.current().nextInt(lower, upper + 1);
    }

    public static <T> T randomElementsFromList(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static <T> List<T> randomElementsFromCollection(Collection<T> collections, int num) {
        if (collections.size() == 0) {
            return collections.stream().toList();
        }

        List<T> result = new ArrayList<>();
        List<T> tmp = new ArrayList<>(collections);
        while (num > result.size() && tmp.size() > 0) {
            int index = randomInt(0, tmp.size());
            result.add(tmp.get(index));
            tmp.remove(index);
        }

        return result;
    }

    /**
     * Apply a chain of int transforms on a given dynamic int.
     * @param input original value
     * @param transforms list of transforms to be applied
     * @param activator object that activates/owns this transforms
     * @return transformed int
     */
    public static DynamicInt applyTransformChain(
            DynamicInt input,
            List<IntTransform> transforms,
            GameObject activator
    ) {
        DynamicInt result = input;
        for (IntTransform t : transforms) {
            result = t.apply(result, activator);
        }
        return result;
    }

    /**
     * Apply a chain of int transforms on a given dynamic int.
     * @param input original value
     * @param transforms list of transforms to be applied
     * @param activator object that activates/owns this transforms
     * @param target object that these transforms are targeting at
     * @return transformed int
     */
    public static DynamicInt applyTransformChain(
            DynamicInt input,
            List<IntTransform> transforms,
            GameObject activator,
            GameObject target
    ) {
        DynamicInt result = input;
        for (IntTransform t : transforms) {
            result = t.apply(result, activator, target);
        }
        return result;
    }
}
