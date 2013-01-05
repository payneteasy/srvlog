package com.payneteasy.srvlog;

import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.adapter.syslog.SyslogAdapterConfig;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Date: 03.01.13 Time: 16:36
 */
//TODO move it to separate integration-tests artifact
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-service.xml", "classpath:spring/spring-dao.xml"})
public class LogManagerTest {

    @Autowired
    private ILogCollector logCollector;

    @Autowired
    private SyslogAdapterConfig logAdapterConfig;

    @Autowired
    private ILogDao logDao;

    @Test
    public void testRunSyslogServer(){
        //Assert.assertEquals("Not run yet", true, logCollector.isStarted());
    }

    @Test
    public void testSendMessage(){
//        SyslogIF udpSyslogClient = Syslog.getInstance(logAdapterConfig.getSyslogProtocol());
//
//        udpSyslogClient.getConfig().setSendLocalName(false);
//        udpSyslogClient.getConfig().setPort(logAdapterConfig.getSyslogPort());
//        udpSyslogClient.info("hello");
//
//        for(int i=0; i<5 && logDao.getLogs().size()<=0; i++){
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                System.out.println("Still 0");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        Assert.assertEquals("hello", logDao.getLogs().get(0));

    }

    @Test
    public void testSyslog4jConfig(){
        Assert.assertEquals(1514, logAdapterConfig.getSyslogPort());
        Assert.assertEquals("udp", logAdapterConfig.getSyslogProtocol());
    }
}
