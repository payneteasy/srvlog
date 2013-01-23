package com.payneteasy.srvlog.ui;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.util.WebContainerUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Date: 09.01.13
 */
public class CommonUiIntegrationTest extends CommonIntegrationTest{
    private static final Logger LOG = LoggerFactory.getLogger(CommonUiIntegrationTest.class);

    private Server server;

    @Override
    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        try {
            startWebapps();
        } catch (Exception e) {
           LOG.error("Cannot start webapps");
        }
    }

    @Override
    protected final void createSpringContext() {
    }

    @Override
    @After
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

        WebAppContext webAppContext = WebContainerUtils.createWebApp( "", "../srvlog-web/src/test/resources/jetty/override-ui-web.xml", server, "../srvlog-web", "../srvlog-web/src/test/resources/jetty/jetty-env-ui.xml");

        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        contextHandlerCollection.setHandlers(new Handler[]{webAppContext});

        server.setHandler(contextHandlerCollection);

        WebContainerUtils.startAndWait(server);
    }

    public Server getServer() {
        return server;
    }

    public static void main(String[] args) {
        try {
            new CommonUiIntegrationTest().startWebapps();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
