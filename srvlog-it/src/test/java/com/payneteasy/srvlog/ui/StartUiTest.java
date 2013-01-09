package com.payneteasy.srvlog.ui;

import com.payneteasy.srvlog.util.SeleniumUtils;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Date: 09.01.13
 */
public class StartUiTest extends CommonUiIntegrationTest{
    private SeleniumUtils seleniumContextTest;

    @Override
    protected ClassPathXmlApplicationContext createSpringContext() {
        return new ClassPathXmlApplicationContext();
    }

    @Override
    protected void createDatabase() throws IOException, InterruptedException {

    }

    @Override
    public void tearDown() {
        super.tearDown();
        seleniumContextTest.releaseWebDriver();
    }

    @Test
    public void startUI() throws InterruptedException {
        seleniumContextTest = new SeleniumUtils();
        seleniumContextTest.initWithLogin();

        seleniumContextTest.getWebDriver().get(seleniumContextTest.url("logs"));

        TimeUnit.SECONDS.sleep(15);
    }
}
