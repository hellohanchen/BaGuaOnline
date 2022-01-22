package com.hellohanchen.baguaserver.entity;

import com.hellohanchen.bagua.GameManager.State;
import com.hellohanchen.bagua.enums.EventType;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EzyObjectBinding
@NoArgsConstructor
public class GameEvent {
    EventType type;
    private String userOrAttacker;
    private String defender;

    // state change
    private State newState;

    // cardPosition/Damage/Heal
    private int positionOrValue;

    // character attack
    private int defenderOrTargetId;
    private int attackerId;
}
