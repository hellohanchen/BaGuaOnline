package com.hellohanchen.baguaserver.controller;

import com.hellohanchen.baguaserver.entity.PlayerCredential;
import com.hellohanchen.baguaserver.entity.PlayerId;
import com.hellohanchen.baguaserver.repo.CredentialRepo;
import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.core.annotation.EzyEventHandler;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.sercurity.EzySHA256;
import com.tvd12.ezyfoxserver.constant.EzyEventNames;
import com.tvd12.ezyfoxserver.constant.EzyLoginError;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import lombok.Setter;

@Setter
@EzySingleton
@EzyEventHandler(EzyEventNames.USER_LOGIN)
public class LoginController
        extends EzyAbstractPluginEventController<EzyUserLoginEvent> {

    @EzyAutoBind
    private CredentialRepo credentialRepo;

    @Override
    public void handle(EzyPluginContext ctx, EzyUserLoginEvent event) {
        logger.info("BaGua Online - user {} login in", event.getUsername());
        String player = event.getUsername();
        if (EzyStrings.isNoContent(player)) {
            throw new EzyLoginErrorException(EzyLoginError.INVALID_USERNAME);
        }

        String password = event.getPassword();
        if (EzyStrings.isNoContent(password)) {
            throw new EzyLoginErrorException(EzyLoginError.INVALID_PASSWORD);
        }

        PlayerId gamePlayerId = new PlayerId(player);
        PlayerCredential credential = credentialRepo.findById(gamePlayerId);
        String sha256Password = EzySHA256.cryptUtfToLowercase(password);
        if (credential != null) {
            if (!credential.password().equals(sha256Password))
                throw new EzyLoginErrorException(EzyLoginError.INVALID_PASSWORD);
        } else {
            credentialRepo.save(new PlayerCredential(gamePlayerId, sha256Password));
        }
    }
}
