package com.payneteasy.srvlog.wicket.component.daterange;

import com.payneteasy.srvlog.util.DateRangeType;
import junit.framework.Assert;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.file.Path;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 18.02.13 Time: 11:49
 */
public class DateRangePageTest {
    WicketTester tester;

    @Before
    public void setUp() throws Exception {
        WebApplication webApplication = new WebApplication() {

            @Override
            protected void init() {
                List<IResourceFinder> resourceFinders = new ArrayList<>();
                resourceFinders.add(new Path("../srvlog-web/src/test/java"));
                resourceFinders.addAll(getResourceSettings().getResourceFinders());
                getResourceSettings().setResourceFinders(resourceFinders);
                mountPage("dateRangePage", TestDateRangePage.class);
            }

            @Override
            public Class<? extends Page> getHomePage() {
                return TestDateRangePage.class;
            }
        };
        tester = new WicketTester(webApplication);
    }

    @Test
    public void testDateRange() throws Exception {
        tester.startPage(TestDateRangePage.class);
        tester.assertRenderedPage(TestDateRangePage.class);

        FormTester formTester = tester.newFormTester("form");
        formTester.select("date-range-panel:date-range-type",2);
        formTester.submit("button");

        DateRangePanel.DateRangeModel dateRangeModel;
        dateRangeModel = (DateRangePanel.DateRangeModel) formTester.getForm().getModelObject();

        Assert.assertEquals(dateRangeModel.getDateRangeType().name(), DateRangeType.THIS_WEEK.name());

    }
}
