package com.hellohanchen.bagua.events;

import com.hellohanchen.bagua.enums.EventType;
import com.hellohanchen.bagua.exceptions.GameProcessException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * EventTracker is a class that contains dictionaries to store events happened in the game.
 * To save memory spaces. All events stored would be cleaned up after rounds end.
 */
public class EventTracker {
    private final HashMap<Integer, Event> events = new HashMap<>();
    private final HashMap<Integer, List<Event>> effectToEvents = new HashMap<>();
    private int counter = 0;

    public int nextId() {
        return ++counter;
    }

    public int lastId() {
        return counter;
    }

    public void clear() {
        events.clear();
        effectToEvents.clear();
    }

    /**
     * Record a new game event.
     * If the event already exists, just add child effects for that event.
     * @param event event
     */
    public void add(Event event) {
        int eventId = event.id();
        int effectId = event.effectId();

        if (!events.containsKey(eventId)) {
            events.put(eventId, event);
        } else {
            // this block is here for supporting adding new child effects for an existing event
            events.get(eventId).childEffects().addAll(event.childEffects());
            return;
        }

        if (effectToEvents.containsKey(effectId)) {
            effectToEvents.get(effectId).add(event);
        } else {
            effectToEvents.put(effectId, List.of(event));
        }
    }

    public void addChildEffectToLast(List<Integer> childEffects) {
        events.get(lastId()).childEffects().addAll(childEffects);
    }

    public boolean containsEvent(int id) {
        return events.containsKey(id);
    }

    /* Getters */
    public Event getEvent(int id) {
        return events.get(id);
    }

    /**
     * Get all character that are killed/destroyed/damaged to death by an effect.
     *
     * @param effectId an effect
     * @return all character ids that are killed by a given effect
     */
    public HashSet<Integer> getObjectIdKilledByEffect(int effectId) {
        return effectToEvents.get(effectId).stream()
                .flatMap(event -> switch (event.type()) {
                    case Death, Destroy -> Stream.of(event.targetId());
                    case Damage -> event.childEffects().stream()
                            .flatMap(childEffect -> effectToEvents.get(childEffect).stream())
                            .filter(childEvent -> childEvent.type() == EventType.Death)
                            .map(Event::targetId);
                    default -> Stream.of();
                })
                .collect(Collectors.toCollection(HashSet::new));
    }

    public int getCharacterIdSummonByEffect(int effectId) {
        Event event = effectToEvents.get(effectId).get(0);
        if (event.type() != EventType.Summon) {
            throw new GameProcessException(
                    "Event Error: Guard main effect event is not SUMMON");
        }

        return event.targetId();
    }
}
