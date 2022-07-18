package com.hellohanchen.bagua;

import com.hellohanchen.bagua.board.Domain;
import com.hellohanchen.bagua.cardholders.PocketLibrary;
import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.cards.GuaCard;
import com.hellohanchen.bagua.cards.SpellCard;
import com.hellohanchen.bagua.characters.Character;
import com.hellohanchen.bagua.characters.Guard;
import com.hellohanchen.bagua.dynamicints.ConstantInt;
import com.hellohanchen.bagua.effects.*;
import com.hellohanchen.bagua.statics.EffectFactory;
import com.hellohanchen.bagua.statics.TargetFactory;
import com.hellohanchen.bagua.target.TargetPQ;
import com.hellohanchen.bagua.constraints.Constraint;
import com.hellohanchen.bagua.effects.auras.Aura;
import com.hellohanchen.bagua.effects.auras.IAuraTarget;
import com.hellohanchen.bagua.effects.record.EffectRecord;
import com.hellohanchen.bagua.effects.record.EffectRecordPQ;
import com.hellohanchen.bagua.enums.*;
import com.hellohanchen.bagua.events.Event;
import com.hellohanchen.bagua.events.EventTracker;
import com.hellohanchen.bagua.exceptions.GameObjectException;
import com.hellohanchen.bagua.exceptions.GameProcessException;
import com.hellohanchen.bagua.interfaces.ICarrier;
import com.hellohanchen.bagua.statics.CardFactory;
import com.hellohanchen.bagua.statics.GameUtils;
import com.hellohanchen.bagua.target.TargetType;
import com.hellohanchen.bagua.visitors.BoolConstraintChecker;
import com.hellohanchen.bagua.visitors.EffectExecutor;
import com.hellohanchen.baguaserver.entity.GameId;
import com.hellohanchen.baguaserver.entity.GameStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hellohanchen.bagua.ConfigConstants.*;
import static com.hellohanchen.bagua.enums.TargetOwnerLogic.ALLY;

/**
 * GameManager is main script on client side.
 * Player call functions in gameManager to play the game.
 * Player is also responsible to deal with different events such as
 * using cards and selecting targets.
 * TODO: split the functions in GameManager to sub scripts if possible
 */
public class GameManager {
    /**
     * Each round would have 4 different stages. start -> playing -> end -> postEnd
     * In each stage, player can make action or some events might be triggered.
     */
    public enum State {
        Init,  // state for setting up networking stuff
        Setup,  // state for setting up game logic and objects
        Start,  // round start
        Playing,  // player playing
        End,  // round end
        PostEnd,  // process effects and characters attack
        Pending,  // for multiplayer game to sync
        Finish  // game progress completed
    }

    /**
     * How does gameManager select target for card usage
     */
    private enum TargetingMode {
        Select,  // local player select targets
        Random,  // randomly select targets
        Provided  // targets are already selected
    }

    /* Game Objects */
    @Getter
    private final GameId id;
    @Getter
    private final Player player1;
    @Getter
    private final Player player2;

    private final ObjectManager objects;

    /* Game Status */
    protected State currentState = State.Init;
    private Player currentRoundOwner;
    private final HashMap<Player, Integer> roundNumbers = new HashMap<>();
    private final HashMap<Player, List<Card>> pendingCards = new HashMap<>();
    private final HashMap<Player, Integer> pendingTargets = new HashMap<>();

    /* Event & Effects */
    private final EffectExecutor executor;
    private final BoolConstraintChecker constraintChecker;

    private int currentEffectId = nullEffectId;
    private int characterCounter = firstCharacterId;
    private final TargetPQ effectTargets = new TargetPQ();
    protected final EventTracker eventTracker = new EventTracker();
    private final EffectRecordPQ effectQueue = new EffectRecordPQ();
    private final List<ICarrier<Aura>> auraCarriers = new ArrayList<>();

    public GameManager(String name1, String name2) {
        this.id = new GameId(name1, name2);
        this.player1 = new Player(name1, 1);
        this.player2 = new Player(name2, 2);
        this.player1.setOpponent(this.player2);
        this.objects = new ObjectManager(this);
        this.executor = new EffectExecutor(this);
        this.constraintChecker = new BoolConstraintChecker(this);
    }

    /* Game State Flow */
    public void process() {
        switch (currentState) {
            case Init -> onInit();
            case Setup -> onSetup();
            case Start -> onStart();
            case Playing -> onPlaying();
            case End -> onEnd();
            case PostEnd -> onPostEnd();
        }
    }

    /**
     * In the Init state, the server would set up players.
     */
    private void onInit() {
        currentState = State.Setup;

        process();
    }

    /**
     * In the Setup state, the game would set up objects in the game board
     */
    private void onSetup() {
        this.objects.addCardHolder(new PocketLibrary(player1, player1CardHolderId));
        this.objects.addCardHolder(new PocketLibrary(player2, player2CardHolderId));
        this.objects.addDomain(new Domain(player1, player1Id, player1DomainId));
        this.objects.addDomain(new Domain(player2, player2Id, player2DomainId));

        this.currentRoundOwner = player1;
        roundNumbers.put(player1, 0);
        roundNumbers.put(player2, 0);

        generateGuaCardForEachPlayer(8);

        currentState = State.Start;

        process();
    }

    /**
     * On start state
     * 1. generate 1 new gua card for each player
     * 2. add roundNumber for the current round owner
     */
    private void onStart() {
        addNewEvent(EventType.RoundStart);

        lastEventTriggersEffect(
                activateEffect(
                        new AddCard(
                                "Generate card on round start",
                                "Generate card for each player on the round start",
                                TargetFactory.cardPocketOf(ALLY),
                                GameUtils.randomInt(0, 7),
                                new ConstantInt(1)),
                        getCardHolder(currentRoundOwner),
                        EffectRecordType.GameState));
        lastEventTriggersEffect(
                activateEffect(
                        new AddCard(
                                "Generate card on round start",
                                "Generate card for each player on the round start",
                                TargetFactory.cardPocketOf(ALLY),
                                GameUtils.randomInt(0, 7),
                                new ConstantInt(1)),
                        getCardHolder(currentRoundOwner.getOpponent()),
                        EffectRecordType.GameState));

        roundNumbers.put(currentRoundOwner, roundNumbers.get(currentRoundOwner) + 1);

        currentState = State.Playing;

        executeEffects();

        process();
    }

    /**
     * On playing state
     * 0. wait until both players submit cards and targets
     * 1. use card for currentRoundOwner and execute the effects
     * 2. use card for other player and execute the effects
     */
    private void onPlaying() {
        if (pendingCards.size() == 2) {
            useCardForPlayer(currentRoundOwner);
            useCardForPlayer(currentRoundOwner.getOpponent());
            currentState = State.End;

            process();
        }
    }

    private void onEnd() {
        addNewEvent(EventType.RoundEnd);
        currentState = State.PostEnd;
        roundEndCharacterAttack();

        process();
    }

    /**
     * On PostEnd state
     * 1. Clean up event tracker
     * 2. Switch round owner
     * 3. Start next round
     */
    private void onPostEnd() {
        eventTracker.clear();
        currentRoundOwner = currentRoundOwner.getOpponent();
        currentState = State.Start;

        pendingCards.clear();
        pendingTargets.clear();

        process();
    }

    public void endGame(Player winner) {
        currentState = State.Finish;
    }

    /* Card Operations */
    public void addCardToPocket(Player player, Card card) {
        this.objects.addCardToPocket(player, card);
    }

    private void generateGuaCardForEachPlayer(int num) {
        for (int i = 0; i < num; i++) {
            addCardToPocket(
                    player1,
                    CardFactory.generateCard(i, 1));
            addCardToPocket(
                    player2,
                    CardFactory.generateCard(i, 1));
        }
    }

    public void upLevelCard(GuaCard card, int up) {
        card.getGua().addLevel(up);
    }

    public void addPendingCard(Player player, Card card, int targetId) {
        if (!pendingCards.containsKey(player)) {
            pendingCards.put(player, List.of(card));
            pendingTargets.put(player, targetId);
        }
    }

    public void addPendingCard(
            Player player, GuaCard card1, GuaCard card2, int targetId
    ) {
        if (!pendingCards.containsKey(player)) {
            pendingCards.put(player, List.of(card1, card2));
            pendingTargets.put(player, targetId);
        }
    }

    public void removeCard(Card card) {
        getCardHolder(card.getOwner()).removeCardAt(card.getPositionInPocket());
    }

    public void drawCard(Player player, int num) {
        PocketLibrary cardHolder = getCardHolder(player);

        for (int i = 0; i < num; i++) {
            if (!cardHolder.isLibraryEmpty()) {
                if (cardHolder.pocketCount() == maxPocketCard) break;

                cardHolder.addCardToPocket(cardHolder.popCardFromLibraryRandomly());
                addNewEvent(EventType.DrawCard, cardHolder);
            } else {
                // TODO: add logic for empty library
                break;
            }
        }
    }

    private void useCardForPlayer(Player player) {
        List<Card> cards = pendingCards.get(player);
        if (cards.size() == 1) {
            Card card = cards.get(0);
            if (card instanceof SpellCard) {
                activateCardEffects(card.getEffects(), player, pendingTargets.get(player));
            } else if (card instanceof GuaCard) {
                activateCardEffects(
                        card.getEffects(),
                        player,
                        pendingTargets.get(player));
            }

            getCardHolder(player).removeCardAt(card.getPositionInPocket());
        } else {
            GuaCard card1 = (GuaCard) cards.get(0);
            GuaCard card2 = (GuaCard) cards.get(1);

            activateCardEffects(
                    EffectFactory.generateEffects(card1.getGua().combine(card2.getGua())),
                    player,
                    pendingTargets.get(player));

            getCardHolder(player).removeCardAt(card1.getPositionInPocket());
            getCardHolder(player).removeCardAt(card2.getPositionInPocket());
        }

        executeEffects();
    }

    private void activateCardEffects(
            List<Effect> effects,
            Player user,
            Integer targetId
    ) {
        if (effects.isEmpty()) {
            throw new GameObjectException("Card to use doesn't have effect.");
        }

        TargetType targetType = effects.get(0).getTargetType();
        GameObject target = null;

        if (targetType.isSelectionNeeded()) {
            if (targetId == 0) {
                throw new GameObjectException("Card to use doesn't have target.");
            }

            target = getObject(targetId);

            // if the target is already destroy, randomly re-select a target
            if (target == null) {
                List<? extends GameObject> newTargets = generateEffectTarget(
                        targetType,
                        getCardHolder(user));
                if (!newTargets.isEmpty()) {
                    target = GameUtils.randomElementsFromList(newTargets);
                    addNewEvent(EventType.UseCard, target);
                } else {
                    addNewEvent(EventType.UseCard);
                }
            } else {
                addNewEvent(EventType.UseCard, target);
            }
        } else {
            addNewEvent(EventType.UseCard);
        }

        GameObject activator = getCardHolder(user);

        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            targetType = effect.getTargetType();

            // activate executionConstraint except for the main effect,
            // which constraint would always be TRUE
            if (i > 0) {
                effect.getExecutionConstraint().activate(
                        activator, effectQueue.lastId(), effectTargets.getTargetId());
            }

            // only the first/main effect would need target selection
            if (targetType.isSelectionNeeded() && target != null) {
                effectTargets.clear();
                effectTargets.select(target);
            }

            lastEventTriggersEffect(activateEffect(
                    effect,
                    activator,
                    EffectRecordType.PlayerAction,
                    effectTargets.getCopy(),
                    nullGeneratorId));
        }
    }

    /* Effect & Event */
    public boolean checkConstraint(Constraint constraint) {
        return constraintChecker.check(constraint);
    }

    protected void executeEffects() {
        /*
         * GameManage would only visit the top effect of effectQueue each time
         * During execution, new effects might be triggered and added to effectQueue
         * Execution would only end when there is no more effect left in the queue
         */
        while (!effectQueue.isEmpty()) {
            EffectRecord record = effectQueue.pop();
            Effect effect = record.effect();
            GameObject activator = record.activator();
            TargetType targetType = effect.getTargetType();

            this.currentEffectId = record.effectId();

            // TODO: log info

            if (!checkConstraint(effect.getExecutionConstraint())) continue;

            if (targetType.isNonObjectTargeting()) {
                executor.visit(effect);
            } else if (targetType.isRuntimeTargeting()) {
                List<? extends GameObject> targets = generateEffectTarget(
                        targetType,
                        activator);

                if (targets.isEmpty()) continue;

                effectTargets.select(targets);
                executor.execute(effect, effectTargets.getCopy(), activator);
            } else {
                executor.execute(effect, record.targets(), activator);
            }
        }
    }

    /**
     * A new effect is activated by the last/current event.
     *
     * @return id of the activated effect.
     */
    public int activateEffect(
            Effect effect,
            GameObject activator,
            EffectRecordType type,
            TargetPQ targets,
            int generatorId
    ) {
        effectQueue.add(new EffectRecord(
                effect,
                type,
                activator,
                effectQueue.nextId(),
                eventTracker.lastId(),
                generatorId,
                targets
        ));
        return effectQueue.lastId();
    }

    /**
     * A new effect is activated by the last/current event.
     *
     * @return id of the activated effect.
     */
    public int activateEffect(
            Effect effect,
            GameObject activator,
            EffectRecordType type
    ) {
        return this.activateEffect(effect, activator, type, null, nullGeneratorId);
    }

    /**
     * Select a new object as target and then activate the effect with that target selection.
     *
     * @return id of the activated effect.
     */
    private int selectThenActivateEffect(
            GameObject target,
            Effect effect,
            GameObject activator,
            EffectRecordType type,
            int generatorId
    ) {
        effectTargets.select(target);
        return activateEffect(effect, activator, type, effectTargets, generatorId);
    }

    /**
     * Record a new event.
     * The event would always be generated by the current effect that is being executed.
     */
    public void addNewEvent(EventType type, GameObject target, GameObject addend) {
        eventTracker.add(new Event(eventTracker.nextId(), type, currentEffectId, target));
    }

    public void addNewEvent(EventType type, GameObject target) {
        eventTracker.add(new Event(eventTracker.nextId(), type, currentEffectId, target));
    }

    public void addNewEvent(EventType type) {
        eventTracker.add(new Event(eventTracker.nextId(), type, currentEffectId));
    }

    public void lastEventTriggersEffect(int childEffect) {
        lastEventTriggersEffects(List.of(childEffect));
    }

    public void lastEventTriggersEffects(List<Integer> childEffects) {
        eventTracker.addChildEffectToLast(childEffects);
    }

    public void generateDeathEffect(Character character) {
        lastEventTriggersEffect(selectThenActivateEffect(
                character,
                new Death(),
                character,
                EffectRecordType.Death,
                nullGeneratorId));
    }

    public void generateDestroyEffect(Character character) {
        lastEventTriggersEffect(selectThenActivateEffect(
                character,
                new Destroy(),
                character,
                EffectRecordType.Destroy,
                nullGeneratorId));
    }

    public boolean targetKilledByEffect(int effectId, int targetId) {
        return eventTracker.getObjectIdKilledByEffect(effectId).contains(targetId);
    }

    public int getCharacterIdSummonedByEffect(int effectId) {
        return eventTracker.getCharacterIdSummonByEffect(effectId);
    }

    /* Auras */
    public void applyAuraForCarrier(ICarrier<Aura> carrier) {
        GameObject activator = carrier.asGameObject();

        carrier.getInner()
                .forEach(a -> {
                    a.activate(activator);

                    generateEffectTarget(a.getTargetType(), activator)
                            .forEach(
                                    t -> t.get(IAuraTarget.class)
                                            .addEffectiveAura(activator.getId(), a));
                });

        auraCarriers.add(carrier);
    }

    public void removeAuraForCarrier(ICarrier<Aura> carrier) {
        GameObject activator = carrier.asGameObject();

        carrier.getInner()
                .forEach(a -> generateEffectTarget(a.getTargetType(), activator)
                        .forEach(t -> t.get(IAuraTarget.class)
                                .removeEffectiveAura(activator.getId())));

        auraCarriers.remove(carrier);
    }

    public void checkAuraForNewCharacter(Character character) {
        auraCarriers.forEach(c -> {
            c.getInner().forEach(a -> {
                TargetType targetType = a.getTargetType();
                if (targetType.targetClass != TargetClass.OccupiedCharacter
                        || targetType.isSelfSelected()) {
                    return;
                }

                if (generateEffectTarget(targetType, c.asGameObject()).stream()
                        .anyMatch(t -> t.equals(character))) {
                    character.addEffectiveAura(c.getId(), a);
                }
            });
        });
    }

    /* Character */
    public void damage(Character character, int damage) {
        character.beDamaged(damage);
        addNewEvent(EventType.Damage, character);

        if (character.getHealthValue() <= 0) {
            generateDeathEffect(character);
        }
    }

    public void death(Character character) {
        addNewEvent(EventType.Death, character);
        remove(character);
        generateDestroyEffect(character);
    }

    public void destroy(Character character) {
        if (character.getType() == CharacterType.CAPTAIN) {
            endGame(character.getOwner().getOpponent());
        }

        addNewEvent(EventType.Destroy, character);
        getDomain(character.getOwner()).death((Guard) character);
    }

    public void heal(Character character, int heal) {
        character.beHealed(heal);
        addNewEvent(EventType.Heal, character);
    }

    public void summon(Character character, Summon effect) {
        Guard g = getDomain(character.getOwner())
                .summon(effect, nextCharacterId(), character.getPosition());
        checkAuraForNewCharacter(g);

        if (g.hasAura()) {
            applyAuraForCarrier(g);
        }

        addNewEvent(EventType.Summon, character);
    }

    public void remove(Character character) {
        effectQueue.removeCharacter(character.getId());
        if (character.hasAura()) {
            removeAuraForCarrier(character);
        }

        // TODO: remove the character from listener list
    }

    /**
     * At the end of each round, characters in domains
     * would attack each other with the followings rules:
     * 1. Character of round owner would be the attacker
     * 2. Characters would attack in order of Jin -> Mu -> Shui -> Huo -> Tu
     * 3. Character would try to attack opponent which has the same position,
     *      if there is not an opponent character, it would attack Captain
     * 4. Captain(Player) characters don't attack
     */
    private void roundEndCharacterAttack() {
        Domain attackers = getDomain(currentRoundOwner);
        Domain defenders = getDomain(currentRoundOwner.getOpponent());

        roundEndCharacterAttack(attackers, defenders, Domain.Position.Jin);
        roundEndCharacterAttack(attackers, defenders, Domain.Position.Mu);
        roundEndCharacterAttack(attackers, defenders, Domain.Position.Shui);
        roundEndCharacterAttack(attackers, defenders, Domain.Position.Huo);
        roundEndCharacterAttack(attackers, defenders, Domain.Position.Tu);
    }

    private void roundEndCharacterAttack(
            Domain attackers,
            Domain defenders,
            Domain.Position position) {
        Character attacker = attackers.getOccupied(position);
        if (attacker != null) {
            Character defender = defenders.getOccupied(position);
            if (defender == null) {
                defender = defenders.getCaptain();
            }
            characterAttack(attacker, defender, EffectRecordType.GameState);
        }

        executeEffects();
    }

    public void characterAttack(
            Character attacker,
            Character defender,
            EffectRecordType type) {
        addNewEvent(EventType.Attack, defender);
        lastEventTriggersEffect(selectThenActivateEffect(
                defender,
                new Attack(attacker, defender),
                attacker,
                type,
                attacker.getId()));
    }

    /* Target */
    public void select(GameObject target) {
        effectTargets.select(target);
    }

    public List<? extends GameObject> generateEffectTarget(
            TargetType targetType,
            Player effectUser,
            GameObject activator
    ) {
        TargetSelectMethod selectMethod = targetType.getTargetSelectMethod();
        TargetOwnerLogic ownerLogic = targetType.getTargetOwnerLogic();

        switch (selectMethod) {
            case Self -> {
                return List.of(activator);
            }
            case Generate -> {
                Player targetPlayer = effectUser.applyTargetLogic(ownerLogic);
                switch (targetType.targetClass) {
                    case CardHolder -> {
                        return List.of(getCardHolder(targetPlayer));
                    }
                    case AvailableCharacter -> {
                        return getDomain(targetPlayer).getAvailableCharacters();
                    }
                    default -> throw new GameProcessException(
                            "Can't auto-select target character.");
                }
            }
            case Select, Random, All, Other -> {
                List<Player> players = ownerLogic == TargetOwnerLogic.ANY
                        ? List.of(player1, player2)
                        : List.of(effectUser.applyTargetLogic(ownerLogic));

                return players.stream()
                        .flatMap(p -> getSelectableTargetObjects(targetType.targetClass, p).stream()
                                .map(o -> (GameObject) o))
                        .filter(o -> selectMethod == TargetSelectMethod.Other && !activator.equals(o))
                        .filter(o -> targetType.targetPredicateOpt.isPresent()
                                && targetType.targetPredicateOpt.get().check(o))
                        .toList();
            }
            default -> throw new IllegalStateException("Unexpected value: " + selectMethod);
        }
    }

    public List<? extends GameObject> generateEffectTarget(
            TargetType targetType,
            GameObject activator
    ) {
        return generateEffectTarget(targetType, activator.getOwner(), activator);
    }

    public List<? extends GameObject> getSelectableTargetObjects(
            TargetClass targetClass,
            Player player
    ) {
        return switch (targetClass) {
            case OccupiedCharacter -> getDomain(player).getOccupiedCharacters(false);
            case OccupiedNonPlayerCharacter -> getDomain(player).getOccupiedCharacters(true);
            case AvailableCharacter -> getDomain(player).getAvailableCharacters();
            case CardHolder -> List.of(getCardHolder(player));
            default -> throw new GameProcessException(
                    "Unsupported owner object selection class: " + targetClass);
        };
    }

    /* Getters */
    public PocketLibrary getCardHolder(Player player) {
        return this.objects.getCardHolder(player);
    }

    public Domain getDomain(Player player) {
        return this.objects.getDomain(player);
    }

    public GameObject getObject(int id) {
        return switch (id) {
            case player1Id -> getDomain(player1).getCaptain();
            case player2Id -> getDomain(player2).getCaptain();
            case player1CardHolderId -> getCardHolder(player1);
            case player2CardHolderId -> getCardHolder(player2);
            case player1DomainId -> getDomain(player1);
            case player2DomainId -> getDomain(player2);
            default -> getDomain(player1).getCharacterById(id).orElse(
                    getDomain(player2).getCharacterById(id).orElse(null));
        };
    }

    public int nextCharacterId() {
        return ++characterCounter;
    }

    public int lastCharacterId() {
        return characterCounter;
    }

    // TODO: track game status/cache game status
    public GameStatus getStatus() {
        GameStatus status = new GameStatus();

        status.setPlayer1(player1.getName());
        status.setPlayer2(player2.getName());
        status.setState(currentState);

        status.setP1Pocket(
                getCardHolder(player1).getCards().stream().map(Card::asData).toList());
        status.setP2Pocket(
                getCardHolder(player2).getCards().stream().map(Card::asData).toList());
        status.setP1Library(getCardHolder(player1).libraryCount());
        status.setP2Library(getCardHolder(player2).libraryCount());

        status.setP1Characters(
                getDomain(player1).getOccupiedCharacters(false)
                        .stream().map(Character::asData).toList());
        status.setP2Characters(
                getDomain(player2).getOccupiedCharacters(false)
                        .stream().map(Character::asData).toList());

        return status;
    }
}
