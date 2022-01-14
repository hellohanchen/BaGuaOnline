package com.hellohanchen.bagua.effects.auras;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.dynamicints.inttransform.IntTransform;
import com.hellohanchen.bagua.effects.BuffCharacter;
import com.hellohanchen.bagua.effects.BuffSpellDamage;
import com.hellohanchen.bagua.effects.Effect;

import java.util.*;

/**
 * AuraHolder stores a list of auras and keeps tracking their carriers.
 */
public class AuraHolder {
    private GameManager gameManager;
    private final HashMap<Integer, List<Aura>> carrierToAuras = new HashMap<>();
    private final LinkedHashMap<Aura, BuffCharacter> buffs = new LinkedHashMap<>();
    private final LinkedHashMap<Aura, BuffSpellDamage> spellDamages = new LinkedHashMap<>();

    public void add(int carrierId, Aura aura) {
        if (!carrierToAuras.containsKey(carrierId)) {
            carrierToAuras.put(carrierId, new ArrayList<>());
        }
        carrierToAuras.get(carrierId).add(aura);

        if (aura.inner() instanceof BuffCharacter buff) {
            buffs.put(aura, buff);
        } else {
            spellDamages.put(aura, (BuffSpellDamage) aura.inner());
        }
    }

    public void remove(int carrierId) {
        carrierToAuras.get(carrierId).forEach(buffs::remove);
        carrierToAuras.get(carrierId).forEach(spellDamages::remove);
        carrierToAuras.remove(carrierId);
    }

    /**
     * Get all Aura effects that are currently valid based on the execution constraint check.
     * @param effects List of Aura effects.
     * @return effects that are valid under current game status
     */
    public <T extends Effect> List<T> getValidEffects(Collection<T> effects) {
        return effects.stream()
                .filter(e -> gameManager.checkConstraint(e.getExecutionConstraint()))
                .toList();
    }

    /* Getters */
    public List<IntTransform> getAttackTransforms() {
        return getValidEffects(buffs.values()).stream()
                .map(e -> e.attack)
                .toList();
    }

    public List<IntTransform> getHealthTransforms() {
        return getValidEffects(buffs.values()).stream()
                .map(e -> e.health)
                .toList();
    }

    public List<IntTransform> getSpellDamageTransforms() {
        return getValidEffects(spellDamages.values()).stream()
                .map(e -> e.spellDamage)
                .toList();
    }

    /* Setters */
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void clear() {
        carrierToAuras.clear();
        buffs.clear();
        spellDamages.clear();
    }
}
