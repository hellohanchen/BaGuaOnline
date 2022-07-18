package com.hellohanchen.baguaserver.entity;

import com.hellohanchen.bagua.GameManager;
import com.hellohanchen.bagua.cards.Card;
import com.hellohanchen.bagua.cards.GuaCard;
import com.hellohanchen.baguaserver.response.GameStatusResponse;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EzyObjectBinding
public class GameStatus {
    private String player1;
    private String player2;
    private GameManager.State state = GameManager.State.Init;
    private List<CardData> p1Pocket = new ArrayList<>();
    private List<CardData> p2Pocket = new ArrayList<>();
    private int p1Library;
    private int p2Library;
    private List<CharacterData> p1Characters = new ArrayList<>();
    private List<CharacterData> p2Characters = new ArrayList<>();

    public GameStatusResponse toResponse(String player) {
        GameStatusResponse response = new GameStatusResponse();
        response.setPlayer1(player1);
        response.setPlayer1(player2);
        response.setState(state);
        response.setP1Library(p1Library);
        response.setP2Library(p2Library);
        response.setP1Characters(p1Characters);
        response.setP2Characters(p2Characters);

        if (player.equals(player1)) {
            response.setPocket(p1Pocket);
            response.setOpponentPocket(p2Pocket.size());
        } else {
            response.setPocket(p2Pocket);
            response.setOpponentPocket(p1Pocket.size());
        }

        return response;
    }

    @Getter
    @Setter
    @Builder
    @EzyObjectBinding
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CardData {
        private int type;
        private String name;
        private String description;
        private int positionInPocket;

        @Builder.Default
        private int cardCode = -1;

        private IntData level;

        public static CardData fromCard(Card card) {
            CardDataBuilder builder =  CardData.builder()
                    .type(card.getType().ordinal())
                    .name(card.getName())
                    .description(card.getDescription())
                    .positionInPocket(card.getPositionInPocket())
                    .cardCode(card.getCardCode());

            if (card instanceof GuaCard guaCard) {
                builder.level(new IntData(
                        guaCard.getLevelValue(),
                        guaCard.getGua().getLevel().isEnhanced(),
                        guaCard.getGua().getLevel().isImpaired()
                ));
            }

            return builder.build();
        }
    }

    @Getter
    @Setter
    @EzyObjectBinding
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CharacterData {
        private int position;
        private int id;
        private String name;
        private String description;
        private boolean hasAura;

        private IntData damage;
        private IntData shield;
        private IntData health;
    }

    @Getter
    @Setter
    @Builder
    @EzyObjectBinding
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IntData {
        private int currentValue;
        @Builder.Default
        private boolean inEnhanced = false;
        @Builder.Default
        private boolean isImpaired = false;
    }
}
