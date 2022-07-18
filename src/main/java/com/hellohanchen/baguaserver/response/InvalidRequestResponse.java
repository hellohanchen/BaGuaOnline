package com.hellohanchen.baguaserver.response;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;

@EzyObjectBinding
public record InvalidRequestResponse(String message) { }
