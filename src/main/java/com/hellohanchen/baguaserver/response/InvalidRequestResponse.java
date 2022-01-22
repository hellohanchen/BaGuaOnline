package com.hellohanchen.baguaserver.response;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyObjectBinding
public record InvalidRequestResponse(String message) { }
