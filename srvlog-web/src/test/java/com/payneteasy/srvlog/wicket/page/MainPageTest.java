package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogCount;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 16.01.13 Time: 13:07
 */
public class MainPageTest extends AbstractWicketTester {
    private IIndexerService indexerService;

    @Test
    public void renderPageTest() throws IndexerServiceException {
        WicketTester wicketTester = getWicketTester();
        predefinedParameter();

        //after onclick
        DateRange lastMonth = DateRange.lastMonth();
        EasyMock.expect(indexerService.numberOfLogsByDate(lastMonth.getFromDate(), lastMonth.getToDate())).andReturn(new HashMap<Date, Long>());
        EasyMock.expect(indexerService.numberOfSeveritiesByDate(lastMonth.getFromDate(), lastMonth.getToDate())).andReturn(new ArrayList<LogCount>());

        DateRange thisWeek = DateRange.thisWeek();
        EasyMock.expect(indexerService.numberOfLogsByDate(thisWeek.getFromDate(), thisWeek.getToDate())).andReturn(new HashMap<Date, Long>());
        EasyMock.expect(indexerService.numberOfSeveritiesByDate(thisWeek.getFromDate(), thisWeek.getToDate())).andReturn(new ArrayList<LogCount>());

        DateRange lastWeek = DateRange.lastWeek();
        EasyMock.expect(indexerService.numberOfLogsByDate(lastWeek.getFromDate(), lastWeek.getToDate())).andReturn(new HashMap<Date, Long>());
        EasyMock.expect(indexerService.numberOfSeveritiesByDate(lastWeek.getFromDate(), lastWeek.getToDate())).andReturn(new ArrayList<LogCount>());

        DateRange thisMonth = DateRange.thisMonth();
        EasyMock.expect(indexerService.numberOfLogsByDate(thisMonth.getFromDate(), thisMonth.getToDate())).andReturn(new HashMap<Date, Long>());
        EasyMock.expect(indexerService.numberOfSeveritiesByDate(thisMonth.getFromDate(), thisMonth.getToDate())).andReturn(new ArrayList<LogCount>());

        EasyMock.replay(indexerService);

        wicketTester.startPage(LogMainPage.class);

        wicketTester.clickLink("button-group-holder:LAST_MONTH");
        wicketTester.clickLink("button-group-holder:THIS_WEEK");
        wicketTester.clickLink("button-group-holder:LAST_WEEK");
        wicketTester.clickLink("button-group-holder:THIS_MONTH");

        EasyMock.verify(indexerService);
    }

    @Test
    public void buttonGroupTest() throws IndexerServiceException {
       WicketTester wicketTester = getWicketTester();
       predefinedParameter();



    }

    private void predefinedParameter() throws IndexerServiceException {
        DateRange dateRange = DateRange.thisMonth();
        EasyMock.expect(indexerService.numberOfLogsByDate(dateRange.getFromDate(), dateRange.getToDate())).andReturn(new HashMap<Date, Long>());
        EasyMock.expect(indexerService.numberOfSeveritiesByDate(dateRange.getFromDate(), dateRange.getToDate())).andReturn(new ArrayList<LogCount>());
    }


    @Override
    protected void setupTest() {
        indexerService = EasyMock.createMock(IIndexerService.class);
        addBean("logCollector", indexerService);
    }




}
