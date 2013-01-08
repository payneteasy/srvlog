package com.payneteasy.srvlog;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Date: 08.01.13
 * Time: 17:07
 */
public class CommonIntegrationTest {

    protected ClassPathXmlApplicationContext context;

    @Before
    public void setUp() throws IOException, InterruptedException {
        DatabaseUtil.createDatabase(new String[]{"bash", "./create_database.sh"});
        context = createSpringContext();

    }

    protected ClassPathXmlApplicationContext createSpringContext() {
        return new ClassPathXmlApplicationContext("classpath:spring/spring-test-datasource.xml","classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml");
    }

    @After
    public void tearDown() {
        if (context != null) {
            context.close();
        }

    }

}
