package com.hellohanchen.bagua.predicates;

/**
 * Predicates would check whether an object satisfies some conditions.
 * @param <T> type of checked objects
 */
public interface IPredicate<T> {
    boolean check(T obj);
}
