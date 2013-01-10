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

        EasyMock.expect(logCollector.loadLatest(25)).andReturn(getTestLogData(25));
        EasyMock.replay(logCollector);

        wicketTester.startPage(OnlineLogMonitorPage.class);
        //wicketTester.assertRenderedPage(OnlineLogMonitorPage.class);

        EasyMock.verify(logCollector);

        EasyMock.reset(logCollector);

        EasyMock.expect(logCollector.loadLatest(50)).andReturn(getTestLogData(50));

        EasyMock.replay(logCollector);

        FormTester formTester = wicketTester.newFormTester("search-form");
        wicketTester.assertComponent("search-form:search-filter-latestChoice", DropDownChoice.class);
        wicketTester.assertComponent("search-form:search-log-data", ListView.class);

//        SEARCH FILTER
        DropDownChoice<Integer> latestChoice = (DropDownChoice<Integer>) wicketTester.getComponentFromLastRenderedPage("search-form:search-filter-latestChoice");
        //Assert.assertEquals("Expected 3 select choices : [25, 50, 100]", 25, latestChoice.getChoices().size());
        formTester.select("search-filter-latestChoice", 1); // 0 is index

        Integer latestChoiceValue = latestChoice.getModelObject();

        formTester.submit("search-btn");

//        ListView<LogData> listView = (ListView) wicketTester.getComponentFromLastRenderedPage("search-form:search-log-data");
//        Assert.assertEquals(latestChoiceValue, Integer.valueOf(listView.getViewSize()));

        EasyMock.verify(logCollector);

    }

    public static List<LogData> getTestLogData(Integer limit) {
        ArrayList<LogData> listData = new ArrayList<LogData>();
        for (int i = 1; i <=limit; i++) {
            LogData logData = new LogData();
            logData.setSeverity(i);
            logData.setFacility(i);
            logData.setHost("localhost");
            logData.setDate(new Date());
            logData.setId(Long.valueOf(i));
            listData.add(logData);
        }
        return listData;
    }
}
