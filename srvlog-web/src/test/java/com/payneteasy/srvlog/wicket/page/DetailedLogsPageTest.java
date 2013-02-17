package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.util.DateRange;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Date: 17.02.13 Time: 18:21
 */
public class DetailedLogsPageTest extends AbstractWicketTester {
    private ILogCollector logCollector;
    private IIndexerService indexerService;
    @Override
    protected void setupTest() {
        logCollector = EasyMock.createMock(ILogCollector.class);
        indexerService = EasyMock.createMock(IIndexerService.class);
        addBean("logCollector", logCollector);
        addBean("indexerService",indexerService);
    }

    @Test
    public void testSideBarNavigation() throws Exception {
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        EasyMock.replay(logCollector);

        wicketTester.startPage(FirewallAlertDataPage.class);
        wicketTester.assertRenderedPage(FirewallAlertDataPage.class);

        wicketTester.clickLink("firewall-drop-container:firewall-drop");
        wicketTester.assertRenderedPage(FirewallDropDataPage.class);

        EasyMock.verify(logCollector);
    }
}
