package com.hellohanchen.bagua.interfaces;

/**
 * ICopy is the interface of objects that can be deeply copied.
 * @param <T> Class implements ICopy.
 */
public interface ICopy<T extends ICopy<T>> {
    /**
     * @return A deep copy of this object.
     */
    T getCopy();
}
