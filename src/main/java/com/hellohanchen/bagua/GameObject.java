package com.hellohanchen.bagua;

import com.hellohanchen.bagua.board.Domain;
import com.hellohanchen.bagua.cardholders.CardHolder;
import com.hellohanchen.bagua.interfaces.Identifiable;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * AstrolabeMono is the MonoBehaviour extension in Astrolabe project.
 * 1. AstrolabeMono implements IIdentifiable, means we can compare their id directly
 * 2. Only AstrolabeMono can be selected as EffectTarget
 * 3. AstrolabeMono keep a reference to its Optional version
 * 4. AstrolabeMono can return its component script without calling MonoBehaviour.GetComponent{T}()
 * 5. AstrolabeMono must have owner and gameManager
 */
public abstract class GameObject implements Identifiable {
    @Getter @Setter
    protected Player owner;
    @Getter @Setter
    protected GameManager gameManager;
    @Getter @Setter
    protected int id;

    protected GameObject() { }

    /* Getters */
    public Optional<GameObject> asOptional() {
        return Optional.of(this);
    }

    public <T> T get(Class<T> typeToGet) {
        if (typeToGet == getClass() || typeToGet.isAssignableFrom(getClass())) {
            //noinspection unchecked
            return (T) this;
        }

        if (typeToGet == CardHolder.class) {
            return gameManager.getCardHolder(owner).get(typeToGet);
        }

        if (typeToGet == Domain.class) {
            return gameManager.getDomain(owner).get(typeToGet);
        }

        return null;
    }
}
