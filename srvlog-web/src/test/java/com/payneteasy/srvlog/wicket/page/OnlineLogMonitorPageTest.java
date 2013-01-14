package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import junit.framework.Assert;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListView;
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
    }

    @Test
    public void renderPage(){
        WicketTester wicketTester = getWicketTester();

        List<LogData> testLogData25 = getTestLogData(25);
        EasyMock.expect(logCollector.loadLatest(25)).andReturn(testLogData25);
        EasyMock.replay(logCollector);

        wicketTester.startPage(OnlineLogMonitorPage.class);

        EasyMock.verify(logCollector);
        EasyMock.reset(logCollector);

        List<LogData> testLogData50 = getTestLogData(50);
        EasyMock.expect(logCollector.loadLatest(50)).andReturn(testLogData50);

        EasyMock.replay(logCollector);

        wicketTester.clickLink("group-button-50");

        EasyMock.verify(logCollector);
        EasyMock.reset(logCollector);
    }

    public static List<LogData> getTestLogData(Integer limit) {
        ArrayList<LogData> listData = new ArrayList<LogData>();
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
}
