package com.payneteasy;


import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import java.io.File;


/**
 * Created on 02.12.2014.
 */
public class StartUIInTomcat {

    public static void main(String[] args) throws ServletException, LifecycleException {
        String webappDirLocation = "src/main/webapp";

        String baseDir = new File("target").getAbsolutePath();
        System.setProperty("catalina.base", baseDir);

        Tomcat tomcat = new Tomcat();

        tomcat.setBaseDir(baseDir);
        tomcat.setPort(8080);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/srvlog?autoReconnect=false&characterEncoding=utf8&useInformationSchema=true&noAccessToProcedureBodies=true");
        dataSource.setUsername("srvlog");
        dataSource.setPassword("123srvlog123");
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        dataSource.setTestOnBorrow(true);
        dataSource.setValidationQuery("call create_collections()");

        SimpleNamingContextBuilder builder = null;
        try {
            builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/comp/env/jdbc/srvlog", dataSource);
        } catch (NamingException e) {
            e.printStackTrace();
        }


        tomcat.addWebapp("/srvlog", new File(webappDirLocation).getAbsolutePath());


        tomcat.start();
        tomcat.getServer().await();
    }
}


