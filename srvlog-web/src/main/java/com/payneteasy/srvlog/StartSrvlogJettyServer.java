package com.payneteasy.srvlog;

import com.payneteasy.srvlog.config.IStartupConfig;
import com.payneteasy.srvlog.websocket.jetty.LogEndpointCreator;
import com.payneteasy.startup.parameters.StartupParametersFactory;
import org.eclipse.jetty.ee10.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.ee10.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.ee10.webapp.Configuration;
import org.eclipse.jetty.ee10.webapp.FragmentConfiguration;
import org.eclipse.jetty.ee10.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.ee10.webapp.MetaInfConfiguration;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.ee10.webapp.WebInfConfiguration;
import org.eclipse.jetty.ee10.webapp.WebXmlConfiguration;
import org.eclipse.jetty.ee10.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class StartSrvlogJettyServer {

    private static final Logger LOG = LoggerFactory.getLogger(StartSrvlogJettyServer.class);

    public static void main(String[] args) throws Exception {

        try {

            IStartupConfig config = StartupParametersFactory.getStartupParameters(IStartupConfig.class);

            Server server = new Server(config.getJettyPort());

            WebAppContext webAppContext = new WebAppContext();
            webAppContext.setContextPath(config.getJettyContext());
            webAppContext.setDefaultsDescriptor("embedded-webapp/WEB-INF/web.xml");
            webAppContext.setDescriptor(config.webDescriptorPath());

            ResourceFactory resourceFactory = ResourceFactory.of(webAppContext);
            // Resolve the webapp directory through a single jar:file URL. ResourceFactory.newClassLoaderResource()
            // may return a combined resource without a URI, which Jetty 12 cannot extract from the uber-jar.
            URL webappUrl = StartSrvlogJettyServer.class.getClassLoader().getResource("embedded-webapp");
            if (webappUrl == null) {
                throw new IllegalStateException("embedded-webapp directory is not found on the classpath");
            }
            Resource webappResource = resourceFactory.newResource(webappUrl);
            webAppContext.setWarResource(webappResource);

            // Jetty 12: jetty-env.xml location is passed as a context attribute instead of EnvConfiguration.setJettyEnvXml()
            webAppContext.setAttribute(EnvConfiguration.JETTY_ENV_XML,
                    resourceFactory.newResource(new File(config.getJettyEnvConfigPath()).toURI().toURL()));

            Configuration[] configurations = new Configuration[]{
                    new WebInfConfiguration(),
                    new WebXmlConfiguration(),
                    new MetaInfConfiguration(),
                    new FragmentConfiguration(),
                    new EnvConfiguration(),
                    new PlusConfiguration(),
                    new JettyWebXmlConfiguration()
            };

            webAppContext.setConfigurations(configurations);
            webAppContext.setServer(server);

            server.setHandler(webAppContext);

            JettyWebSocketServletContainerInitializer.configure(webAppContext, (servletContext, wsContainer) ->
            {
                wsContainer.setMaxTextMessageSize(config.webSocketMaxMessageSize());
                wsContainer.setIdleTimeout(Duration.ofSeconds(config.webSocketIdleTimeoutSeconds()));
                wsContainer.addMapping(config.webSocketEndpointPath(), new LogEndpointCreator());
            });

            LOG.info("Starting jetty srvlog server on port {}, context path: {}",
                    config.getJettyPort(), config.getJettyContext());

            server.start();
            server.setStopAtShutdown(true);

            LOG.info("jetty srvlog server started");

        } catch (Exception e) {
            LOG.error("Cannot start server app", e);
            System.exit(1);
        }
    }
}
