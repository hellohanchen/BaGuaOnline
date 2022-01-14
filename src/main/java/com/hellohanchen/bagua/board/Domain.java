package com.hellohanchen.bagua.board;

import com.hellohanchen.bagua.GameObject;
import com.hellohanchen.bagua.Player;
import com.hellohanchen.bagua.characters.Character;
import com.hellohanchen.bagua.characters.Captain;
import com.hellohanchen.bagua.characters.Guard;
import com.hellohanchen.bagua.effects.Summon;
import com.hellohanchen.bagua.exceptions.GameObjectException;

import java.util.*;

import static com.hellohanchen.bagua.ConfigConstants.firstCharacterId;

/**
 * Domain is the gameObject that characters deployed on and make actions.
 * Each character position in domain has different name.
 */
public class Domain extends GameObject {
    public enum Position {
        Captain,
        Jin,
        Mu,
        Shui,
        Huo,
        Tu
    }

    public static HashMap<Position, Guard> placeHolder = new HashMap<>() {{
        put(Position.Jin, new Guard(Position.Jin));
        put(Position.Mu, new Guard(Position.Mu));
        put(Position.Shui, new Guard(Position.Shui));
        put(Position.Huo, new Guard(Position.Huo));
        put(Position.Tu, new Guard(Position.Tu));
    }};

    private final HashMap<Position, Character> occupied = new HashMap<>();

    public Domain(Player owner, int playerId, int id) {
        this.owner = owner;
        Captain captain = new Captain(owner, playerId);
        captain.setDomain(this);
        this.occupied.put(Position.Captain, captain);
        this.id = id;
        placeHolder.get(Position.Jin).setId(playerId * 10 + 20 + 1);
        placeHolder.get(Position.Mu).setId(playerId * 10 + 20 + 2);
        placeHolder.get(Position.Shui).setId(playerId * 10 + 20 + 3);
        placeHolder.get(Position.Huo).setId(playerId * 10 + 20 + 4);
        placeHolder.get(Position.Tu).setId(playerId * 10 + 20 + 5);
    }

    /* Character Operations */

    /**
     * Summon a guard at a position
     *
     * @param effect effect to summon the guard
     * @param id character id
     * @param position targeted position
     * @return summoned guard object
     */
    public Guard summon(Summon effect, int id, Position position) {
        if (occupied.containsKey(position)) {
            throw new GameObjectException("Position already has guard on it");
        }

        Guard guard = new Guard(effect, id, position);
        occupied.put(position, guard);
        guard.setDomain(this);

        return guard;
    }

    public void death(Guard guard) {
        assert guard.getOwner() == getOwner();
        occupied.remove(guard.getPosition());
    }

    /* Getters */
    public Character getCaptain() {
        return this.occupied.get(Position.Captain);
    }

    public int getOccupiedCharacterCount() {
        return occupied.size();
    }

    public List<Character> getOccupiedCharacters(boolean excludeCaptain) {
        return excludeCaptain
                ? occupied.entrySet().stream()
                .filter(e -> e.getKey() != Position.Captain)
                .map(Map.Entry::getValue)
                .toList()
                : occupied.values().stream().toList();
    }

    public Character getOccupied(Position position) {
        return occupied.get(position);
    }

    public Optional<Character> getCharacterById(int id) {
        return id < firstCharacterId
                ? placeHolder.values().stream().filter(p -> p.getId() == id).findFirst()
                        .map(p -> p.get(Character.class))
                : getOccupiedCharacterById(id);
    }

    public Optional<Character> getOccupiedCharacterById(int id) {
        return occupied.values().stream().filter(c -> c.getId() == id).findFirst();
    }

    public List<Position> getAvailablePositions() {
        return Arrays.stream(Position.values())
                .filter(p -> !occupied.containsKey(p))
                .toList();
    }

    public List<Guard> getAvailableCharacters() {
        return getAvailablePositions().stream().map(p -> placeHolder.get(p)).toList();
    }
}
