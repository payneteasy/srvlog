package com.payneteasy.srvlog;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ListenerHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.NamingException;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

public class WebServer  {

    private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

    public static void main(String[] args) throws Exception {
        try {
            LOG.info(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            Server server = new WebServer().create();

            /*while (System.in.available() == 0) {
                Thread.sleep(5000);
            }
            server.stop();*/
            //server.dumpStdErr();
            server.start();
            server.join();
        } catch (Exception e) {
            LOG.error("Cannot start server", e);
            System.exit(100);
        }
    }

    Server create() throws Exception {

        final Server server = new Server();

        // HTTP connector
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8080);
        //http.setIdleTimeout(30000);

        // Set the connector
        server.addConnector(http);

        WebAppContext srvlog = new WebAppContext();
        srvlog.setContextPath("/srvlog");
        srvlog.setBaseResource(Resource.newClassPathResource("www"));


        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/srvlog?characterEncoding=utf8&amp;useInformationSchema=true&amp;noAccessToProcedureBodies=true&amp;useLocalSessionState=true&amp;autoReconnect=false");
        dataSource.setUsername("srvlog");
        dataSource.setPassword("123srvlog123");
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery("call create_collections()");

        org.eclipse.jetty.plus.jndi.Resource jndiResource = new org.eclipse.jetty.plus.jndi.Resource(
                "java:/comp/env/jdbc/srvlog",
                dataSource
        );

        srvlog.setInitParameter("contextConfigLocation",
                "            classpath:spring/spring-ui-datasource.xml\n" +
                "            classpath:spring/spring-dao.xml\n" +
                "            classpath:spring/spring-service.xml\n" +
                "            classpath:spring/spring-log-adapter.xml\n");

        srvlog.getServletHandler().addListener(new ListenerHolder(ContextLoaderListener.class));

        final FilterHolder fh = srvlog.addFilter(WicketFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        fh.setInitParameter("applicationClassName", "com.payneteasy.srvlog.wicket.SrvlogUIApplication");
        fh.setInitParameter("configuration", "DEPLOYMENT");
        fh.setInitParameter("ignorePaths", "/management/version.txt");
        fh.setInitParameter("filterMappingUrlPattern", "/*");

        srvlog.addServlet(ShowVersionServlet.class, "/management/version.txt");

        server.setHandler(srvlog);
/*
        HttpConfiguration httpConfig = new HttpConfiguration();
        */
/*httpConfig.setSecureScheme(HttpScheme.HTTPS.asString());
        httpConfig.setOutputBufferSize(32 * 1024);
        httpConfig.setRequestHeaderSize(8 * 1024);
        httpConfig.setResponseHeaderSize(8 * 1024);
        httpConfig.setHeaderCacheSize(512);
        httpConfig.setSendServerVersion(false);
        httpConfig.setSendDateHeader(false);
        httpConfig.setSendXPoweredBy(false);*//*

        httpConfig.addCustomizer(new ForwardedRequestCustomizer());

        ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        httpConnector.setHost("localhost");
        httpConnector.setPort(8080);

        server.setConnectors(new Connector[]{httpConnector});

        WebAppContext srvlog = new WebAppContext();
        srvlog.setDefaultsDescriptor("WEB-INF/web.xml");

        // Access the SessionHandler from the context.
        SessionHandler sessions = srvlog.getSessionHandler();
        sessions.setMaxInactiveInterval(7200);
        // Explicitly set Session Cache and null Datastore.
        // This is normally done by default,
        // but is done explicitly here for demonstration.
        // If more than one context is to be deployed, it is
        // simpler to use SessionCacheFactory and/or
        // SessionDataStoreFactory instances set as beans on
        // the server.
        SessionCache cache = new DefaultSessionCache(sessions);
        cache.setSessionDataStore(new FileSessionDataStore());
        sessions.setSessionCache(cache);
        srvlog.setTempDirectory(Files.createTempDirectory(null).toFile());
        srvlog.setContextPath("/srvlog/");
        srvlog.setBaseResource(Resource.newClassPathResource("."));
        srvlog.setWar(".");
        srvlog.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");

        */
/*srvlog.getSessionHandler().setHttpOnly(false);
        srvlog.getSessionHandler().setSecureRequestOnly(false);
        srvlog.getSessionHandler().setUsingCookies(true);*//*


        EnvConfiguration envConfiguration = new EnvConfiguration();

        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/srvlog?autoReconnect=false&amp;characterEncoding=utf8&amp;useInformationSchema=true&amp;noAccessToProcedureBodies=true");
        dataSource.setUsername("srvlog");
        dataSource.setPassword("123srvlog123");
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery("call create_collections()");

        org.eclipse.jetty.plus.jndi.Resource jndiResource = new org.eclipse.jetty.plus.jndi.Resource(
                "java:/comp/env/jdbc/srvlog",
                dataSource
        );
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
        srvlog.setConfigurations(configurations);

        ContextHandlerCollection webapps = new ContextHandlerCollection();
        webapps.setHandlers(new Handler[]{srvlog});
        server.setHandler(webapps);
*/

        return server;

    }

}
