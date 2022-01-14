package com.hellohanchen.bagua.predicates.objectPredicates;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.predicates.IPredicate;

/**
 * BaseTargetPredicate is used for filtering out AstrolabeMono objects
 * which are the targets in Astrolabe.
 */
public abstract class ObjectPredicate implements IPredicate<GameObject> {
    public abstract boolean check(GameObject obj);
}
