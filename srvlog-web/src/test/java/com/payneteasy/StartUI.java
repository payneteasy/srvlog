package com.payneteasy;

import com.payneteasy.srvlog.websocket.jetty.LogEndpointCreator;
import org.eclipse.jetty.ee10.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.ee10.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.ee10.webapp.*;
import org.eclipse.jetty.ee10.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;


/**
 * Date: 21.12.12 Time: 16:09
 */
public class StartUI {
    private  static final Logger LOG = LoggerFactory.getLogger(StartUI.class);

    public static void main(String[] args) throws MalformedURLException {
        new StartUI().startServerUI();
    }

    private void startServerUI() throws MalformedURLException {

        LOG.info("Configuring jetty web server ...");

        final Server server = new Server();

        final File tempDir = new File("target/temp");
        tempDir.mkdirs();

        ServerConnector connector = new ServerConnector(server);
        // Set some timeout options to make debugging easier.
        connector.setIdleTimeout(1000 * 60 * 60);
        //connector.setSoLingerTime(-1);
        connector.setPort(Integer.parseInt(System.getProperty("jetty.port", "8080")));

        server.setConnectors(new Connector[]{connector});

        WebAppContext srvlog = new WebAppContext();
        srvlog.setTempDirectory(tempDir);
        srvlog.setServer(server);
        srvlog.setContextPath("/srvlog");
        srvlog.setWar("src/main/webapp");
        srvlog.setDefaultsDescriptor("src/main/webapp/WEB-INF/web.xml");

        srvlog.getSessionHandler().setHttpOnly(false);
        srvlog.getSessionHandler().setSecureRequestOnly(false);
        srvlog.getSessionHandler().setUsingCookies(true);

        // Jetty 12: jetty-env.xml location is passed as a context attribute instead of EnvConfiguration.setJettyEnvXml()
        srvlog.setAttribute(EnvConfiguration.JETTY_ENV_XML,
                ResourceFactory.of(srvlog).newResource(new File("src/test/resources/jetty/jetty-env-ui.xml").toURI().toURL()));
        Configuration[] configurations = new Configuration[]{
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                new EnvConfiguration(),
                new PlusConfiguration(),
                new JettyWebXmlConfiguration()
        };
        srvlog.setConfigurations(configurations);

        JettyWebSocketServletContainerInitializer.configure(srvlog, (servletContext, wsContainer) ->
        {
            wsContainer.setMaxTextMessageSize(65535);
            wsContainer.setIdleTimeout(Duration.ofSeconds(300));
            wsContainer.addMapping("/ws-log", new LogEndpointCreator());
        });

        ContextHandlerCollection webapps = new ContextHandlerCollection();
        webapps.setHandlers(List.of(srvlog));
        server.setHandler(webapps);

        try {
            LOG.info(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();

            while (System.in.available() == 0) {
                Thread.sleep(5000);
            }
            server.stop();
            server.join();
        } catch (Exception e) {
            LOG.error("Cannot start server", e);
            System.exit(100);
        }
    }
}
