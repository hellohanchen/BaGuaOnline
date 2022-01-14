package com.hellohanchen.bagua.interfaces;

public interface Identifiable {
    int getId();

    default boolean equals(Identifiable other) {
        return getId() == other.getId();
    }
}
