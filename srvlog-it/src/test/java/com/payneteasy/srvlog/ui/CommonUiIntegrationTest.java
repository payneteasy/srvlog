package com.payneteasy.srvlog.ui;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.util.WebContainerUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Date: 09.01.13
 */
public class CommonUiIntegrationTest extends CommonIntegrationTest{
    private static final Logger LOG = LoggerFactory.getLogger(CommonUiIntegrationTest.class);

    private Server server;

    @Override
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        try {
            startWebapps();
        } catch (Exception e) {
           LOG.error("Cannot start webapps");
        }
    }

    @Override
    public void tearDown() {
        super.tearDown();
        try {
            WebContainerUtils.shutDownContainer(server);
        } catch (Exception e) {
            LOG.error("Cannot stop webapps");
        }
    }

    protected void startWebapps() throws Exception {
        server = WebContainerUtils.createServer();

        WebAppContext webAppContext = WebContainerUtils.createWebApp( "", "", server, "../srvlog-web", "../srvlog-web/src/test/resources/jetty/jetty-env-ui.xml");

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        contextHandlerCollection.setHandlers(new Handler[]{webAppContext});

        server.setHandler(contextHandlerCollection);

        WebContainerUtils.startAndWait(server);
    }

    public Server getServer() {
        return server;
    }
}
