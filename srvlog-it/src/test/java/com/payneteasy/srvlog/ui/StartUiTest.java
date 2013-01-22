package com.payneteasy.srvlog.ui;

import com.nesscomputing.syslog4j.SyslogFacility;
import com.nesscomputing.syslog4j.SyslogLevel;
import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.util.SeleniumUtils;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 09.01.13
 */
public class StartUiTest extends CommonUiIntegrationTest{

    private SeleniumUtils seleniumContextTest;

    @Override
    @After
    public void tearDown() {
        super.tearDown();
        seleniumContextTest.releaseWebDriver();
    }

    @Test @Ignore
    public void startUI() throws InterruptedException {
        seleniumContextTest = new SeleniumUtils();
        seleniumContextTest.initWithLogin();

        seleniumContextTest.getWebDriver().get(seleniumContextTest.url("logs"));

        TimeUnit.SECONDS.sleep(3);
    }




}
