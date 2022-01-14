package com.hellohanchen.bagua.common;

import com.hellohanchen.bagua.exceptions.GameObjectException;
import com.hellohanchen.bagua.interfaces.ICopy;

import java.util.Optional;
import java.util.function.Function;

public class CopyOptional<T extends ICopy<T>> implements ICopy<CopyOptional<T>> {
    public static final CopyOptional<?> Null = new CopyOptional<>();

    private Optional<T> opt;

    private CopyOptional(T value) {
        this.opt = Optional.of(value);
    }

    private CopyOptional() {
        this.opt = Optional.empty();
    }

    public static <T extends ICopy<T>> CopyOptional<T> of(T v) {
        return new CopyOptional<>(v);
    }

    public boolean isPresent() {
        return opt.isPresent();
    }

    public T get() {
        if (opt.isEmpty()) {
            throw new GameObjectException("Error: Null value optional.");
        }

        return opt.get();
    }

    public <R> R getOrElse(Function<T, R> func, R defaultValue) {
        return isPresent() ? func.apply(get()) : defaultValue;
    }

    public CopyOptional<T> getCopy() {
        return isPresent() ? new CopyOptional<>(get().getCopy()) : (CopyOptional<T>) Null;
    }
}
