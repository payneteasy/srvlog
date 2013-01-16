package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Test;

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
    public void testNavigationBar(){
        WicketTester wicketTester = getWicketTester();
        wicketTester.startPage(BasePage.class);
        wicketTester.assertRenderedPage(BasePage.class);

        wicketTester.clickLink("logs-container:logs");
        wicketTester.assertRenderedPage(LogMonitorPage.class);

        wicketTester.clickLink("main-container:main");
        wicketTester.assertRenderedPage(MainPage.class);

    }
}
