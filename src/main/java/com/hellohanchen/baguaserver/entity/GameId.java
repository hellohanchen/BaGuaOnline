package com.hellohanchen.baguaserver.entity;

import com.tvd12.ezyfox.database.annotation.EzyCollectionId;

@EzyCollectionId
public record GameId(String player1, String player2) { }
