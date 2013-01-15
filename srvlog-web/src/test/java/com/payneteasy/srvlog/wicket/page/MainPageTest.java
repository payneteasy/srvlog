package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 14.01.13 Time: 21:18
 */
public class MainPageTest extends AbstractWicketTester {
    private ILogCollector logCollector;

    @Override
    protected void setupTest() {
        logCollector = EasyMock.createMock(ILogCollector.class);
        addBean("logCollector", logCollector);
    }

    @Test
    public void testDateRangeFilter() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();

        DateRange today = DateRange.today();
        List<LogData> searchLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        DateRange thisMonth = DateRange.thisMonth();
        EasyMock.expect(logCollector.search(thisMonth.getFromDate(), thisMonth.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.replay(logCollector);

        wicketTester.startPage(MainPage.class);

        //TODO add check for default load page

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.select("date-range-type", 4);

        formTester.submit("search-button");

        EasyMock.verify(logCollector);
    }

    @Test
    public void testPager() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();
        List<LogData> testLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(testLogData);
        // this call is necessary to expect because wicketTester checks for component visibility
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(testLogData);
//        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 25, 26)).andReturn(testLogData);
//        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(getTestLogData);
//        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 50, 26)).andReturn(getTestLogData);
//        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(getTestLogData);
        EasyMock.replay(logCollector);
        wicketTester.startPage(MainPage.class);
        Component component = wicketTester.getComponentFromLastRenderedPage("table-logs:pager:paging-next", false);
        wicketTester.clickLink(component);

        //wicketTester.clickLink("table-logs:pager:paging-next");
//        wicketTester.clickLink("table-logs:pager:paging-next");
//        wicketTester.clickLink("table-logs:pager:paging-first");
        EasyMock.verify(logCollector);
    }

    @Test
    public void testDefaultParameter() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();

        List<LogData> searchLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.replay(logCollector);

        wicketTester.startPage(MainPage.class);

        EasyMock.verify(logCollector);

    }

    @Test
    public void testIndexingExceptionOccured() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();
        IndexerServiceException indexerServiceException = new IndexerServiceException("While calling indexing service", new Exception());
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andThrow(indexerServiceException);

        EasyMock.replay(logCollector);
        wicketTester.startPage(MainPage.class);
        wicketTester.assertComponent("feedback-panel", FeedbackPanel.class);
        wicketTester.assertErrorMessages("Error while retrieving log data");
        EasyMock.verify(logCollector);

    }

    private List<LogData> getTestLogData() {
        ArrayList<LogData> listData = new ArrayList<LogData>();
        for (int i = 1; i <=30; i++) {
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
