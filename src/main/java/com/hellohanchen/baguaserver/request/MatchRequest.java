package com.hellohanchen.baguaserver.request;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyObjectBinding
public class MatchRequest {
    private String gameName;
}
