package com.payneteasy.srvlog.websocket;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

public class WebSocketEndpointsConfiguration implements ServerApplicationConfig {

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {

        Set<ServerEndpointConfig> result = new HashSet<>();

        if (endpointClasses.contains(WebSocketLogEndpoint.class)) {
            result.add(ServerEndpointConfig.Builder.create(WebSocketLogEndpoint.class, "/ws-log").build());
        }

        return result;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {

        Set<Class<?>> results = new HashSet<>();
        for (Class<?> clazz : scanned) {
            if (clazz.getPackage().getName().contains("websocket")) {
                results.add(clazz);
            }
        }

        return results;
    }
}