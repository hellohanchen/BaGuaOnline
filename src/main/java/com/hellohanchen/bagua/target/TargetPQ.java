package com.hellohanchen.bagua.target;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.interfaces.ICopy;
import com.hellohanchen.bagua.interfaces.Identifiable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class TargetPQ extends ArrayList<GameObject> implements ICopy<TargetPQ> {
    @Getter
    private Optional<GameObject> selectedTarget = Optional.empty();

    public TargetPQ(TargetPQ other) {
        this.addAll(other);
        selectedTarget = other.selectedTarget;
    }

    public TargetPQ() { }

    @Override
    public boolean add(GameObject object) {
        if (contains(object)) {
            return false;
        }

        if (isEmpty()) {
            return super.add(object);
        } else {
            // binary search to find the position for the new effect
            int left = 0;
            int right = size() - 1;
            while (left < right) {
                int mid = (left + right) / 2;
                if (higherThan(object, get(mid))) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            if (higherThan(object, get(left))) {
                this.add(left, object);
            } else {
                this.add(left + 1, object);
            }
        }

        return true;
    }

    public void removeId(int id) {
        this.stream().filter(t -> t.getId() == id).forEach(this::remove);
    }

    /* Target Selection */

    /**
     * Clear selection and then select a new target.
     * @param target new target
     */
    public void select(GameObject target) {
        clear();
        this.add(target);
        selectedTarget = Optional.of(target);
    }

    /**
     * Clear selection and add a list of objects. There is not "selected" target in this case.
     * @param targets a list of targets
     */
    public void select(Collection<? extends GameObject> targets) {
        clear();
        targets.forEach(this::add);
    }

    /**
     * Clear selection and copy selection from another effectTargetPQ.
     * @param other existing selection
     */
    public void select(TargetPQ other) {
        clear();
        this.selectedTarget = other.selectedTarget;
        addAll(other);
    }

    /* Getters */
    public int getTargetId() {
        return selectedTarget.map(GameObject::getId).orElse(0);
    }

    public boolean isSelected() {
        return selectedTarget.isPresent();
    }

    public TargetPQ getCopy() {
        return new TargetPQ(this);
    }

    public <T extends GameObject> Stream<T> mapTo(Class<T> typeToGet) {
        return stream().map(o -> o.get(typeToGet));
    }

    /* Comparator */

    /**
     * @param a object
     * @param b object
     * @return True if the first element has higher priority than the second one.
     */
    private boolean higherThan(Identifiable a, Identifiable b) {
        return selectedTarget.isPresent() && a.getId() == selectedTarget.get().getId()
                || a.getId() < b.getId();
    }
}
