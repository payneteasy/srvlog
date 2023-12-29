package com.payneteasy.srvlog.util;

import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

/**
 * Date: 09.01.13
 */
public class WebContainerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(WebContainerUtils.class);

    public static int PORT = 8080;
    public static String CONTEXT = "/";
    public static String DOMAIN = "localhost";


    public static Server createServer() {
        return createServer(new int[]{PORT});
    }

    public static Server createServer(int[] ports) {
        Server server = new Server();
        Connector[] connectors = new Connector[ports.length];

        for (int i = 0; i < ports.length; i++) {
            int port = ports[i];
            ServerConnector connector = new ServerConnector(server);
            // Set some timeout options to make debugging easier.
            connector.setIdleTimeout(1000 * 60 * 60);
            connector.setPort(Integer.parseInt(System.getProperty("jetty.port", String.valueOf(port))));
            connectors[i] = connector;
        }

        server.setConnectors(connectors);
        return server;
    }

    public static WebAppContext createWebApp(
             String extraClasspath
            , String overrideDescriptor
            , Server server
            , String webProjectPath
            , String jettyEnvFile
    ) throws IOException {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(server);
        webAppContext.setContextPath(CONTEXT);
        webAppContext.setWar(webProjectPath + "/src/main/webapp");

        EnvConfiguration envConfiguration = new EnvConfiguration();
        envConfiguration.setJettyEnvXml(new File(jettyEnvFile).toURI().toURL());

        Configuration[] configurations = new Configuration[]{
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration(),
                envConfiguration,
                new PlusConfiguration(),
                new JettyWebXmlConfiguration()
        };
        webAppContext.setConfigurations(configurations);
        if (extraClasspath != null) {
            webAppContext.setExtraClasspath(extraClasspath);
        }
        if (overrideDescriptor != null) {
            webAppContext.setOverrideDescriptors(Collections.singletonList(overrideDescriptor));
        }

        return webAppContext;
    }

    public static void shutDownContainer(Server server) throws Exception {
        if (server != null) {
            server.stop();
            server.join();
        }
    }

    public static void startContainer(Server server) throws Exception {
        if (server != null) {
            server.start();
            server.join();
        }
    }

    public static void startAndWait(Server server) throws Exception {
        server.start();

        // wait for started
        for (int i = 0; i < 60 && !server.isStarted(); i++) {
            if (i == 0) {
                LOG.info("Waiting jetty server on port {} to be started...", ((ServerConnector)server.getConnectors()[0]).getPort());
            }
            LOG.info("   waiting {}", i);
            Thread.sleep(500);
        }
    }

    public static void stopAndWait(Server server) throws Exception {
        server.stop();

        for (int i = 0; i < 260 && server.isRunning(); i++) {
            LOG.info("   waiting running {}", i);
            Thread.sleep(500);
        }

        for (int i = 0; i < 260 && server.isStopping(); i++) {
            LOG.info("   waiting stopping {}", i);
            Thread.sleep(500);
        }
        for (int i = 0; i < 260 && !server.isStopped() && !server.isStopping() && !server.isRunning(); i++) {
            LOG.info("   waiting stopped {}", i);
            Thread.sleep(500);
        }
    }


}
