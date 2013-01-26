package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Date: 16.01.13 Time: 13:07
 */
public class MainPageTest extends AbstractWicketTester {

    @Test
    @Ignore
    public void renderPageTest(){
        WicketTester wicketTester = getWicketTester();
        wicketTester.startPage(LogMainPage.class);
        wicketTester.assertRenderedPage(LogMainPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
