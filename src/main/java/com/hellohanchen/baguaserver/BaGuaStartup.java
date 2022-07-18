package com.hellohanchen.baguaserver;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.ext.EzyAbstractAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestAppSingletonController;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;

public class BaGuaStartup {

    private static final String ZONE_NAME = "bagua-online";
    private static final String APP_NAME = "bagua-online";
    private static final String PLUGIN_NAME = "bagua-online";

    public static void main(String[] args) throws Exception {
        EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
                .name(PLUGIN_NAME)
                .addListenEvent(EzyEventType.USER_LOGIN)
                .entryLoader(BaGuaGamePluginEntryLoader.class);

        EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
                .name(APP_NAME)
                .entryLoader(BaGuaGameAppEntryLoader.class);

        EzySimpleUserManagementSetting userManagementSetting = new EzyUserManagementSettingBuilder()
                .allowChangeSession(true) // allow change user's session, default true
                .maxSessionPerUser(5) // set number of max sessions per user // default 5
                .userMaxIdleTimeInSecond(999) // set max idle time of a user, default 0
                .userNamePattern("^[a-z0-9_.]{3,36}$") // set username pattern, default ^[a-z0-9_.]{3,36}$
                .build();

        EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
                .name(ZONE_NAME)
                .application(appSettingBuilder.build())
                .userManagement(userManagementSetting)
                .plugin(pluginSettingBuilder.build());

        EzyWebSocketSettingBuilder webSocketSettingBuilder = new EzyWebSocketSettingBuilder()
                .active(true);

        EzyUdpSettingBuilder udpSettingBuilder = new EzyUdpSettingBuilder()
                .active(true);

        EzySessionManagementSettingBuilder sessionManagementSettingBuilder = new EzySessionManagementSettingBuilder()
                .sessionMaxWaitingTimeInSecond(999)
                .sessionMaxIdleTimeInSecond(999)
                .sessionMaxRequestPerSecond(
                        new EzySessionManagementSettingBuilder.EzyMaxRequestPerSecondBuilder()
                                .value(250)
                                .build()
                );

        EzySimpleSettings settings = new EzySettingsBuilder()
                .zone(zoneSettingBuilder.build())
                .websocket(webSocketSettingBuilder.build())
                .udp(udpSettingBuilder.build())
                .sessionManagement(sessionManagementSettingBuilder.build())
                .build();

        EzyEmbeddedServer server = EzyEmbeddedServer.builder()
                .settings(settings)
                .build();
        server.start();
    }

    public static class SpaceGameAppEntry extends EzySimpleAppEntry {

        @Override
        protected String[] getScanableBeanPackages() {
            return new String[]{
                    "com.hellohanchen.baguaserver"
            };
        }

        @Override
        protected String[] getScanableBindingPackages() {
            return new String[]{
                    "com.hellohanchen.baguaserver"
            };
        }

        @Override
        protected EzyAppRequestController newUserRequestController(EzyBeanContext beanContext) {
            return EzyUserRequestAppSingletonController.builder()
                    .beanContext(beanContext)
                    .build();
        }

    }

    public static class BaGuaGameAppEntryLoader extends EzyAbstractAppEntryLoader {

        @Override
        public EzyAppEntry load() {
            return new SpaceGameAppEntry();
        }

    }

    public static class BaGuaGamePluginEntry extends EzySimplePluginEntry {

        @Override
        protected String[] getScanableBeanPackages() {
            return new String[]{
                    "com.hellohanchen.baguaserver"
            };
        }
    }

    public static class BaGuaGamePluginEntryLoader extends EzyAbstractPluginEntryLoader {

        @Override
        public EzyPluginEntry load() {
            return new BaGuaGamePluginEntry();
        }
    }
}
