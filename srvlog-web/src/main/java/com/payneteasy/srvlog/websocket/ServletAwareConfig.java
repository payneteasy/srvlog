package com.payneteasy.srvlog.websocket;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ServletAwareConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {

        HttpSession httpSession = (HttpSession) request.getHttpSession();
        ILogBroadcastingService logBroadcastingService = WebApplicationContextUtils
                .getWebApplicationContext(httpSession.getServletContext()).getBean(ILogBroadcastingService.class);

        config.getUserProperties().put(WebSocketLogEndpoint.LOG_BROADCASTING_SERVICE_KEY, logBroadcastingService);
    }

}