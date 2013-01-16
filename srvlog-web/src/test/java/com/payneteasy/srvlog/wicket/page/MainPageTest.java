package com.payneteasy.srvlog.wicket.page;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

/**
 * Date: 16.01.13 Time: 13:07
 */
public class MainPageTest extends AbstractWicketTester {
    @Override
    protected void setupTest() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Test
    public void renderPageTest(){
        WicketTester wicketTester = getWicketTester();
        wicketTester.startPage(MainPage.class);
        wicketTester.assertRenderedPage(MainPage.class);
    }
}
