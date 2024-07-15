package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 09.01.13
 */
public class OnlineLogMonitorPageTest extends AbstractWicketTester{

    private ILogCollector logCollector;

    @Override
    protected void setupTest() {
        logCollector = EasyMock.createMock(ILogCollector.class);
        addBean("logCollector", logCollector);
        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();
        EasyMock.expect(logCollector.loadHosts()).andReturn(getHosts()).anyTimes();
    }

    @Test
    public void renderPage(){
        WicketTester wicketTester = getWicketTester();
        List<LogData> testLogData25 = getTestLogData(25);
        EasyMock.expect(logCollector.loadLatest(25, null)).andReturn(testLogData25);
        EasyMock.replay(logCollector);

        wicketTester.startPage(OnlineLogMonitorPage.class);

        EasyMock.verify(logCollector);
    }

    @Test
    public void testChoiceHost() {
        WicketTester wicketTester = getWicketTester();

        List<LogData> testLogData25 = getTestLogData(25);
        EasyMock.expect(logCollector.loadLatest(25, null)).andReturn(testLogData25);

        EasyMock.expect(logCollector.loadLatest(25, 1L)).andReturn(testLogData25);
        EasyMock.replay(logCollector);

        wicketTester.startPage(OnlineLogMonitorPage.class);
        FormTester formTester = wicketTester.newFormTester("hostChoice-form");
        formTester.select("choices-host", 1);

    }

    public static List<LogData> getTestLogData(Integer limit) {
        ArrayList<LogData> listData = new ArrayList<>();
        for (int i = 1; i <=limit; i++) {
            LogData logData = new LogData();
            logData.setSeverity(1);
            logData.setFacility(1);
            logData.setHost("localhost");
            logData.setDate(new Date());
            logData.setId(Long.valueOf(i));
            listData.add(logData);
        }
        return listData;
    }

    public static List<HostData> getHosts(){
        List<HostData> hosts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HostData hostData = new HostData();
            hostData.setId(Long.valueOf(i));
            hostData.setIpAddress("");
            hostData.setHostname("host"+i);
            hosts.add(hostData);
        }
        return hosts;
    }
}
