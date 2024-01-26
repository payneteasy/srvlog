package com.payneteasy.srvlog;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Date: 08.01.13
 * Time: 17:07
 */
public abstract class CommonIntegrationTest {

    protected ClassPathXmlApplicationContext context;

    @Before
    public void setUp() throws IOException, InterruptedException {
        createDatabase();
        createSpringContext();
    }

    protected void createSpringContext() {
        context =  new ClassPathXmlApplicationContext("classpath:spring/spring-test-datasource.xml","classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml");
    }

    protected void createDatabase() throws IOException {
        DatabaseUtil.cleanAndMigrateDatabase();
    }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }
    }

}
