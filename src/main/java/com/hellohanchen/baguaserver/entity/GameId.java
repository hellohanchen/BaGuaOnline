package com.hellohanchen.baguaserver.entity;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.database.annotation.EzyCollectionId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EzyObjectBinding
@EzyCollectionId
@NoArgsConstructor
@AllArgsConstructor
public class GameId {
    private String player1;
    private String player2;
}
