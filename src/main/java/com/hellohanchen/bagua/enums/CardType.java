package com.hellohanchen.bagua.enums;

/**
 * Each card has one and only one type, usually determined by the main effect type.
 */
public enum CardType {
    General, // all types of cards
    Gua,
    Assist, // card that can summon assist at a target position
    Spell, // "magic" card
    Equip // equip card would give player character equipments
}
