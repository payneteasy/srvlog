package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.*;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.wicket.page.detailed.FirewallAlertDataPage;
import com.payneteasy.srvlog.wicket.page.detailed.FirewallDropDataPage;
import com.payneteasy.srvlog.wicket.page.detailed.OssecAlertDataPage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

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

        DateRange today = DateRange.today();
        EasyMock.expect(logCollector.getFirewallAlertData(today.getFromDate())).andReturn(Collections.<FirewallAlertData>emptyList());
        EasyMock.expect(logCollector.getFirewallDropData(today.getFromDate())).andReturn(Collections.<FireWallDropData>emptyList());

        EasyMock.replay(logCollector);

        wicketTester.startPage(FirewallAlertDataPage.class);
        wicketTester.assertRenderedPage(FirewallAlertDataPage.class);

        wicketTester.clickLink("firewall-drop-container:firewall-drop");
        wicketTester.assertRenderedPage(FirewallDropDataPage.class);

        EasyMock.verify(logCollector);
    }

    @Test
    public void testFirewallAlertPage() throws Exception {
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        Date now = getNowDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        DateRange today = DateRange.today();
        EasyMock.expect(logCollector.getFirewallAlertData(today.getFromDate())).andReturn(Collections.<FirewallAlertData>emptyList());

        EasyMock.expect(logCollector.getFirewallAlertData(simpleDateFormat.parse(simpleDateFormat.format(now)))).andReturn(Collections.<FirewallAlertData>emptyList());

        EasyMock.replay(logCollector);

        wicketTester.startPage(FirewallAlertDataPage.class);
        wicketTester.assertRenderedPage(FirewallAlertDataPage.class);

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.setValue("date-field", simpleDateFormat.format(now));
        formTester.submit("button");

        EasyMock.verify(logCollector);
    }

    @Test
    public void testNoDataPanel() throws Exception {
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        DateRange today = DateRange.today();
        EasyMock.expect(logCollector.getFirewallAlertData(today.getFromDate())).andReturn(Arrays.asList(new FirewallAlertData[]{new FirewallAlertData()}));

        EasyMock.replay(logCollector);

        wicketTester.startPage(FirewallAlertDataPage.class);
        wicketTester.assertRenderedPage(FirewallAlertDataPage.class);
        wicketTester.assertInvisible("no-data");

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.setValue("date-field", "");
        formTester.submit("button");
        wicketTester.assertVisible("no-data");

        EasyMock.verify(logCollector);
    }

    @Test
    public void testFirewallDropPage() throws Exception {
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        Date now = getNowDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        DateRange today = DateRange.today();
        EasyMock.expect(logCollector.getFirewallDropData(today.getFromDate())).andReturn(Collections.<FireWallDropData>emptyList());

        EasyMock.expect(logCollector.getFirewallDropData(simpleDateFormat.parse(simpleDateFormat.format(now)))).andReturn(Collections.<FireWallDropData>emptyList());

        EasyMock.replay(logCollector);

        wicketTester.startPage(FirewallDropDataPage.class);
        wicketTester.assertRenderedPage(FirewallDropDataPage.class);

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.setValue("date-field", simpleDateFormat.format(now));
        formTester.submit("button");

        EasyMock.verify(logCollector);
    }

    @Test
    public void testOssecAlertPage() throws Exception {
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        Date now = getNowDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        DateRange today = DateRange.today();
        EasyMock.expect(logCollector.getOssecAlertData(today.getFromDate())).andReturn(Collections.<OssecAlertData>emptyList());

        EasyMock.expect(logCollector.getOssecAlertData(simpleDateFormat.parse(simpleDateFormat.format(now)))).andReturn(Collections.<OssecAlertData>emptyList());

        EasyMock.replay(logCollector);

        wicketTester.startPage(OssecAlertDataPage.class);
        wicketTester.assertRenderedPage(OssecAlertDataPage.class);

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.setValue("date-field", simpleDateFormat.format(now));
        formTester.submit("button");

        EasyMock.verify(logCollector);

    }

    private Date getNowDate(){
        return new Date();
    }
}
