package com.payneteasy.srvlog.websocket.jetty;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.websocket.server.JettyWebSocketCreator;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LogEndpointCreator implements JettyWebSocketCreator {

    @Override
    public Object createWebSocket(JettyServerUpgradeRequest request,
                                  JettyServerUpgradeResponse response) {

        ServletContext sc = ((HttpSession) request.getSession()).getServletContext();

        ILogBroadcastingService logBroadcastingService = WebApplicationContextUtils
                .getWebApplicationContext(sc).getBean(ILogBroadcastingService.class);

        return new WebSocketLogEndpoint(logBroadcastingService);
    }
}
