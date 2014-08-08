package com.payneteasy;

import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;


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



        SocketConnector connector = new SocketConnector();
        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(Integer.parseInt(System.getProperty("jetty.port", "8080")));

        server.setConnectors(new Connector[]{connector});

        WebAppContext srvacc = new WebAppContext();
        srvacc.setTempDirectory(tempDir);
        srvacc.setServer(server);
        srvacc.setContextPath("/");
        srvacc.setWar("src/main/webapp");
        EnvConfiguration envConfiguration = new EnvConfiguration();
        envConfiguration.setJettyEnvXml(new File("src/test/resources/jetty/jetty-env-ui.xml").toURI().toURL());
        Configuration[] configurations = new Configuration[]{
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                envConfiguration,
                new PlusConfiguration(),
                new JettyWebXmlConfiguration()
        };
        srvacc.setConfigurations(configurations);


        ContextHandlerCollection webapps = new ContextHandlerCollection();
        webapps.setHandlers(new Handler[]{srvacc});
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
