package com.hellohanchen.baguaserver.entity;

import com.hellohanchen.bagua.GameManager.State;
import com.hellohanchen.bagua.enums.EventType;
import com.hellohanchen.bagua.events.Event;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
@Builder
@EzyObjectBinding
@AllArgsConstructor
@NoArgsConstructor
public class GameEvent {
    EventType type;
    private int eId;  // event id
    private int pId;  // player id

    // state change
    private State newState;

    // cardPosition/Damage/Heal
    private int positionOrValue;

    // character attack
    private int defChrId;
    private int atkChrId;

    // game objects
    @Singular
    private List<GameStatus.CardData> cards;

    public GameEvent(Event event) {
        this.type = event.type();
    }
}
