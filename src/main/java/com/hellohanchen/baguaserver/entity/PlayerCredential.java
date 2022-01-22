package com.hellohanchen.baguaserver.entity;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;

@EzyCollection
public record PlayerCredential(@EzyId PlayerId id, String password) {
}
