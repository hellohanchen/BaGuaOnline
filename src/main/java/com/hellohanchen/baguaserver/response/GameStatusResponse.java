package com.hellohanchen.baguaserver.response;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.baguaserver.entity.GameStatus;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EzyObjectBinding
public class GameStatusResponse {
    private String player1;
    private String player2;
    private GameManager.State state = GameManager.State.Init;
    private List<GameStatus.CardData> pocket = new ArrayList<>();
    private int opponentPocket;
    private int p1Library;
    private int p2Library;
    private List<GameStatus.CharacterData> p1Characters = new ArrayList<>();
    private List<GameStatus.CharacterData> p2Characters = new ArrayList<>();
}
