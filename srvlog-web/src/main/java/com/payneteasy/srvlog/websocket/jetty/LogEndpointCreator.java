package com.payneteasy.srvlog.websocket.jetty;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import jakarta.servlet.ServletContext;
import org.eclipse.jetty.ee10.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.ee10.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketCreator;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LogEndpointCreator implements JettyWebSocketCreator {

    @Override
    public Object createWebSocket(JettyServerUpgradeRequest request,
                                  JettyServerUpgradeResponse response) {

        ServletContext sc = request.getHttpServletRequest().getServletContext();

        ILogBroadcastingService logBroadcastingService = WebApplicationContextUtils
                .getWebApplicationContext(sc).getBean(ILogBroadcastingService.class);

        return new WebSocketLogEndpoint(logBroadcastingService);
    }
}
