package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Date: 16.01.13 Time: 13:07
 */
public class DashboardPageTest extends AbstractWicketTester {
    private IIndexerService indexerService;
    private ILogCollector logCollector;

    @Before
    public void setUp() {
        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();
    }

    @Test
    public void renderPageTest() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();

        predefinedParameter();

        EasyMock.replay(indexerService, logCollector);

        wicketTester.startPage(DashboardPage.class);

        EasyMock.verify(indexerService);
    }

    @Test
    public void testDrillDownPage() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        predefinedParameter();

        //in LogMonitorPage
        expectedInLogMonitorPage();

        EasyMock.replay(indexerService, logCollector);

        wicketTester.startPage(DashboardPage.class);

        wicketTester.clickLink("list-severity-holder:list-severity:0:link");

        wicketTester.assertRenderedPage(LogMonitorPage.class);

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.submit("search-button");

        EasyMock.verify(indexerService);
    }

    private void expectedInLogMonitorPage() throws IndexerServiceException {
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<>());
        DateRange thisMonth = DateRange.thisMonth();
        TreeMap<LogLevel, Long> severityMap = (TreeMap<LogLevel, Long>) getDefaultLogsBySeverityMap();
        LogLevel firstLogLevel = severityMap.firstKey();
        EasyMock.expect(logCollector.search(thisMonth.getFromDate(), thisMonth.getToDate(), null, Arrays.asList(firstLogLevel.getValue()), null, null, 0, 26)).andReturn(new ArrayList<>());
        //after submit
        EasyMock.expect(logCollector.search(thisMonth.getFromDate(), thisMonth.getToDate(), new ArrayList<>(), Arrays.asList(firstLogLevel.getValue()), new ArrayList<>(), null, 0, 26)).andReturn(new ArrayList<>());
    }

    @Test
    public void buttonGroupTest() throws IndexerServiceException {

       WicketTester wicketTester = getWicketTester();
       predefinedParameter();

        //after onclick
        DateRange lastMonth = DateRange.lastMonth();
        EasyMock.expect(indexerService.numberOfLogsByDate(lastMonth.getFromDate(), lastMonth.getToDate())).andReturn(new HashMap<>());
        EasyMock.expect(indexerService.numberOfLogsBySeverity(lastMonth.getFromDate(), lastMonth.getToDate())).andReturn(getDefaultLogsBySeverityMap());

        DateRange thisWeek = DateRange.thisWeek();
        EasyMock.expect(indexerService.numberOfLogsByDate(thisWeek.getFromDate(), thisWeek.getToDate())).andReturn(new HashMap<>());
        EasyMock.expect(indexerService.numberOfLogsBySeverity(thisWeek.getFromDate(), thisWeek.getToDate())).andReturn(getDefaultLogsBySeverityMap());

        DateRange lastWeek = DateRange.lastWeek();
        EasyMock.expect(indexerService.numberOfLogsByDate(lastWeek.getFromDate(), lastWeek.getToDate())).andReturn(new HashMap<>());
        EasyMock.expect(indexerService.numberOfLogsBySeverity(lastWeek.getFromDate(), lastWeek.getToDate())).andReturn(getDefaultLogsBySeverityMap());

        DateRange thisMonth = DateRange.thisMonth();
        EasyMock.expect(indexerService.numberOfLogsByDate(thisMonth.getFromDate(), thisMonth.getToDate())).andReturn(new HashMap<>());
        EasyMock.expect(indexerService.numberOfLogsBySeverity(thisMonth.getFromDate(), thisMonth.getToDate())).andReturn(getDefaultLogsBySeverityMap());

        EasyMock.replay(indexerService, logCollector);

        wicketTester.startPage(DashboardPage.class);

        wicketTester.clickLink("button-group-holder:LAST_MONTH");
        wicketTester.clickLink("button-group-holder:THIS_WEEK");
        wicketTester.clickLink("button-group-holder:LAST_WEEK");
        wicketTester.clickLink("button-group-holder:THIS_MONTH");

        EasyMock.verify(indexerService);
    }

    private void predefinedParameter() throws IndexerServiceException {
        DateRange dateRange = DateRange.thisMonth();
        EasyMock.expect(indexerService.numberOfLogsByDate(dateRange.getFromDate(), dateRange.getToDate())).andReturn(new HashMap<>());
        EasyMock.expect(indexerService.numberOfLogsBySeverity(dateRange.getFromDate(), dateRange.getToDate())).andReturn(getDefaultLogsBySeverityMap());
    }

    @Override
    protected void setupTest() {
        indexerService = EasyMock.createMock(IIndexerService.class);
        logCollector = EasyMock.createMock(ILogCollector.class);
        addBean("indexerService", indexerService);
        addBean("logCollector", logCollector);
    }
}
