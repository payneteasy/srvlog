package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Date: 16.01.13 Time: 13:10
 */
public class BasePageTest extends AbstractWicketTester{
    private ILogCollector logCollector;
    @Override
    protected void setupTest() {
        logCollector = EasyMock.createMock(ILogCollector.class);
        addBean("logCollector", logCollector);
    }

    @Test
    @Ignore
    public void testNavigationBar() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        wicketTester.startPage(BasePage.class);
        wicketTester.assertRenderedPage(BasePage.class);

        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        DateRange today = DateRange.today();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0,26)).andReturn(new ArrayList<LogData>());
        EasyMock.replay(logCollector);

        wicketTester.clickLink("logs-container:logs");
        wicketTester.assertRenderedPage(LogMonitorPage.class);

        EasyMock.verify(logCollector);

        wicketTester.clickLink("main-container:main");
        wicketTester.assertRenderedPage(LogMainPage.class);

    }
}
