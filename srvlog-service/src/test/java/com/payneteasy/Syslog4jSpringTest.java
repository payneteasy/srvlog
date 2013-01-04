package com.payneteasy;

import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.SyslogIF;
import com.payneteasy.dao.ILogManagerDao;
import com.payneteasy.service.ILogManager;
import com.payneteasy.service.ILogManagerConfig;
import com.sun.tools.doclets.internal.toolkit.util.SourceToHTMLConverter;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * Date: 03.01.13 Time: 16:36
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-service.xml", "classpath:spring/spring-dao.xml"})
public class Syslog4jSpringTest {

    @Autowired
    private ILogManager logManager;

    @Autowired
    private ILogManagerConfig logManagerConfig;

    @Autowired
    private ILogManagerDao logManagerDao;

    @Test
    public void testRunSyslogServer(){
        Assert.assertEquals("Not run yet", true, logManager.isStarted());
    }

    @Test
    public void testSendMessage(){
        SyslogIF udpSyslogClient = Syslog.getInstance(logManagerConfig.getSyslog4jProtocol());

        udpSyslogClient.getConfig().setSendLocalName(false);
        udpSyslogClient.getConfig().setPort(logManagerConfig.getSyslog4jPort());
        udpSyslogClient.info("hello");

        for(int i=0; i<5 && logManagerDao.getLogs().size()<=0; i++){
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Still 0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals("hello", logManagerDao.getLogs().get(0));

    }

    @Test
    public void testSyslog4jConfig(){
        Assert.assertEquals(1514, logManagerConfig.getSyslog4jPort());
        Assert.assertEquals("udp", logManagerConfig.getSyslog4jProtocol());
    }
}
