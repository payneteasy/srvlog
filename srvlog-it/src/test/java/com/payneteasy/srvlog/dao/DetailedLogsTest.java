package com.payneteasy.srvlog.dao;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.data.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Date: 19.02.13 Time: 11:56
 */
public class DetailedLogsTest extends CommonIntegrationTest {
    private ILogDao logDao;
    private Date nowDate;
    private String firewallHostName = "firewall";
    private String ossecHostName = "ossec";

    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        logDao = context.getBean(ILogDao.class);
        nowDate = new Date();

        initFireWallHost();
        putFireWallAlertLog();
        putFirewallAlertDropLog();

        initOssecHost();
        putOssecAlertLog();
    }

    @Test
    public void testFirewallAlert() throws Exception {
        List<FirewallAlertData> firewallAlertData = logDao.getFirewallAlertData(nowDate);

        Assert.assertEquals(firewallAlertData.size(), 1);
    }

    @Test
    public void testFireWallDrop() throws Exception {
        List<FirewallDropData> firewallDropDataList = logDao.getFirewallDropData(nowDate);

        Assert.assertEquals(firewallDropDataList.size(), 1);
    }

    @Test
    public void testOssecAlert() throws Exception {
       List<OssecAlertData> ossecAlertData = logDao.getOssecAlertData(nowDate);

       Assert.assertEquals(ossecAlertData.size(), 1);

    }

    private void putFirewallAlertDropLog() {
        LogData logData = new LogData();
        logData.setSeverity(1);
        logData.setDate(nowDate);
        logData.setFacility(1);
        logData.setMessage("Drop packet: drop packet snort ");
        logData.setHost(firewallHostName);
        logData.setProgram(null);
        logDao.saveLog(logData);
    }


    private void putFireWallAlertLog() {
        LogData logData = new LogData();
        logData.setSeverity(1);
        logData.setDate(nowDate);
        logData.setFacility(1);
        logData.setMessage("[Classification: snort message");
        logData.setHost(firewallHostName);
        logData.setProgram("snort");
        logDao.saveLog(logData);
    }

    private void putOssecAlertLog() {
        LogData logData = new LogData();
        logData.setSeverity(1);
        logData.setDate(nowDate);
        logData.setFacility(1);
        logData.setMessage("ossec: Rule: ");
        logData.setHost(ossecHostName);
        logData.setProgram(null);
        logDao.saveLog(logData);
    }



    private void initFireWallHost() {
        //init host with name is firewall
        HostData hostData = new HostData();
        hostData.setHostname(firewallHostName);
        hostData.setIpAddress("127.0.0.1");
        logDao.saveHost(hostData);
    }

    private void initOssecHost() {
        //init host with name is ossec
        HostData hostData = new HostData();
        hostData.setHostname(ossecHostName);
        hostData.setIpAddress("127.0.0.1");
        logDao.saveHost(hostData);
    }


}
