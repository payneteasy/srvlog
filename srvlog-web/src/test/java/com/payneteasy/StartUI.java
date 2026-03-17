package com.payneteasy;

import com.payneteasy.srvlog.websocket.jetty.LogEndpointCreator;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.*;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Duration;


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

        String baseDir = System.getProperty("srvlog.web.dir", System.getenv("SRVLOG_WEB_DIR"));
        if (baseDir == null || baseDir.isEmpty()) {
            // Derive from class location: .../srvlog-web/target/test-classes/com/payneteasy/StartUI.class
            String classPath = StartUI.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            File testClassesDir = new File(classPath.replace("%20", " "));
            baseDir = testClassesDir.getParentFile().getParentFile().getParentFile().getAbsolutePath();
        }

        final Server server = new Server();

        final File tempDir = new File(baseDir, "target/temp");
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
        srvlog.setWar(new File(baseDir, "src/main/webapp").getAbsolutePath());
        srvlog.setDefaultsDescriptor(new File(baseDir, "src/main/webapp/WEB-INF/web.xml").getAbsolutePath());

        srvlog.getSessionHandler().setHttpOnly(false);
        srvlog.getSessionHandler().setSecureRequestOnly(false);
        srvlog.getSessionHandler().setUsingCookies(true);

        EnvConfiguration envConfiguration = new EnvConfiguration();
        String envXmlPath = System.getenv("JETTY_ENV_XML");
        File envFile = envXmlPath != null && !envXmlPath.isEmpty()
                ? new File(envXmlPath)
                : new File(baseDir, "src/test/resources/jetty/jetty-env-ui.xml");
        envConfiguration.setJettyEnvXml(envFile.toURI().toURL());
        Configuration[] configurations = new Configuration[]{
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                envConfiguration,
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
        webapps.setHandlers(new Handler[]{srvlog});
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
