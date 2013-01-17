package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.util.DateRangeType;
import junit.framework.Assert;
import org.apache.wicket.Component;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Date: 14.01.13 Time: 21:18
 */
public class LogMonitorPageTest extends AbstractWicketTester {
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
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        DateRange thisMonth = DateRange.thisMonth();
        EasyMock.expect(logCollector.search(thisMonth.getFromDate(), thisMonth.getToDate(), new ArrayList<Integer>(), new ArrayList<Integer>(),new ArrayList<Integer>(), null, 0, 26)).andReturn(searchLogData);
        EasyMock.replay(logCollector);

        wicketTester.startPage(LogMonitorPage.class);

        //TODO add check for default load page

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.select("date-range-type", 4);

        formTester.submit("search-button");

        EasyMock.verify(logCollector);
    }

    @Test
    public void testDateRangeValidation() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();

        DateRange today = DateRange.today();
        List<LogData> searchLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        EasyMock.expect(logCollector.search(null, null, null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.replay(logCollector);

        wicketTester.startPage(LogMonitorPage.class);

        wicketTester.assertNoErrorMessage();

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.select("date-range-type", 6);
        wicketTester.executeAjaxEvent("form:date-range-type", "onchange");

        formTester.select("date-range-type", 6);
        formTester.submit("search-button");

        wicketTester.assertErrorMessages("Date from is required", "Date to is required");

        EasyMock.verify(logCollector);

    }

    @Test
    public void testExactlyDateRangeFilter() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();
        List<LogData> searchLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());

        DateRange exactly = DateRange.thisMonth();
        EasyMock.expect(logCollector.search(exactly.getFromDate(), exactly.getToDate(), new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(), null, 0, 26)).andReturn(searchLogData);
        EasyMock.replay(logCollector);

        wicketTester.startPage(LogMonitorPage.class);
        wicketTester.assertInvisible("form:holder-exactly-dateRange");

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.select("date-range-type", 7); //6 - date, 7 - time

        wicketTester.executeAjaxEvent("form:date-range-type", "onchange");

        wicketTester.assertVisible("form:holder-exactly-dateRange");
        wicketTester.assertVisible("form:holder-exactly-dateRange:timeFrom-field");
        wicketTester.assertVisible("form:holder-exactly-dateRange:timeTo-field");

        formTester.select("date-range-type", 6); //6 - date, 7 - time

        wicketTester.executeAjaxEvent("form:date-range-type", "onchange");

        wicketTester.assertVisible("form:holder-exactly-dateRange");
        wicketTester.assertVisible("form:holder-exactly-dateRange:dateFrom-field");
        wicketTester.assertVisible("form:holder-exactly-dateRange:dateTo-field");

        formTester.setValue("holder-exactly-dateRange:dateFrom-field", "1.1.2013");
        formTester.setValue("holder-exactly-dateRange:dateTo-field", "1.2.2013");

        formTester.select("date-range-type", 6); //6 - date, 7 - time

        formTester.submit("search-button");

        EasyMock.verify(logCollector);

    }

    @Test
    public void testSearchForPattern() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();
        List<LogData> searchLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(), "search me", 0, 26)).andReturn(searchLogData);
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        EasyMock.replay(logCollector);

        wicketTester.startPage(LogMonitorPage.class);

        FormTester formTester = wicketTester.newFormTester("form");

        formTester.setValue("pattern", "search me");

        formTester.submit("search-button");

        EasyMock.verify();
    }


    @Test
    public void testPaging() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();
        List<LogData> testLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(testLogData);
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 25, 26)).andReturn(testLogData);
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(testLogData);
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 25, 26)).andReturn(testLogData);
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(), null, 0, 26)).andReturn(testLogData);

        EasyMock.replay(logCollector);
        wicketTester.startPage(LogMonitorPage.class);
        wicketTester.clickLink("paging-navigator:paging-next");
        wicketTester.clickLink("paging-navigator:paging-previous");

        wicketTester.clickLink("paging-navigator:paging-next");

        FormTester form = wicketTester.newFormTester("form");

        form.submit("search-button");

        EasyMock.verify(logCollector);

    }

    @Test
    public void testDefaultParameter() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();

        List<LogData> searchLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        EasyMock.replay(logCollector);

        wicketTester.startPage(LogMonitorPage.class);

        EasyMock.verify(logCollector);

    }

    @Test
    public void testIndexingExceptionOccured() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        DateRange today = DateRange.today();
        IndexerServiceException indexerServiceException = new IndexerServiceException("While calling indexing service", new Exception());
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andThrow(indexerServiceException);
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        EasyMock.replay(logCollector);
        wicketTester.startPage(LogMonitorPage.class);
        wicketTester.assertComponent("feedback-panel", FeedbackPanel.class);
        wicketTester.assertErrorMessages("Error while retrieving log data: While calling indexing service");
        EasyMock.verify(logCollector);

    }

    @Test
    public void testListMultipleSearch() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();

        DateRange today = DateRange.today();
        List<LogData> searchLogData = getTestLogData();
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), null, null, null, null, 0, 26)).andReturn(searchLogData);
        EasyMock.expect(logCollector.loadHosts()).andReturn(new ArrayList<HostData>());
        EasyMock.expect(logCollector.search(today.getFromDate(), today.getToDate(), Arrays.asList(LogFacility.kern.getValue(), LogFacility.user.getValue()), Arrays.asList(LogLevel.EMERGENCY.getValue()), new ArrayList<Integer>(), null, 0, 26)).andReturn(searchLogData);
        EasyMock.replay(logCollector);

        wicketTester.startPage(LogMonitorPage.class);
        wicketTester.assertRenderedPage(LogMonitorPage.class);

        ListMultipleChoice<LogLevel> severityListMultipleChoice = (ListMultipleChoice<LogLevel>) wicketTester.getComponentFromLastRenderedPage("form:severity-choice");
        Assert.assertEquals(LogLevel.values().length, severityListMultipleChoice.getChoices().size());

        ListMultipleChoice<LogLevel> facilityListMultipleChoice = (ListMultipleChoice<LogLevel>) wicketTester.getComponentFromLastRenderedPage("form:facility-choice");
        Assert.assertEquals(LogFacility.values().length, facilityListMultipleChoice.getChoices().size());

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.select("severity-choice", 0);

        formTester.select("facility-choice", 0);
        formTester.select("facility-choice", 1);

        formTester.submit("search-button");

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
