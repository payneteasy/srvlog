package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.util.tester.FormTester;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TerminalPageTest extends AbstractWicketTester {

    private ILogBroadcastingService logBroadcastingService;

    @Override
    protected void setupTest() {

        List<String> hostNameList = Arrays.asList("hostname-1", "hostname-2");
        List<String> programNameList = Arrays.asList("program-1", "program-2");

        ILogCollector logCollector = EasyMock.createMock(ILogCollector.class);
        addBean("logCollector", logCollector);

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        logBroadcastingService = EasyMock.createMock(ILogBroadcastingService.class);
        addBean("logBroadcastingService", logBroadcastingService);

        EasyMock.expect(logBroadcastingService.getHostNameList()).andReturn(hostNameList);
        EasyMock.expect(logBroadcastingService.getProgramNameList()).andReturn(programNameList);

        EasyMock.replay(logCollector);
        EasyMock.replay(logBroadcastingService);

        getWicketTester().startPage(TerminalPage.class);
    }

    @Test
    public void renderPageTest() {
        EasyMock.verify(logBroadcastingService);
    }

    @Test
    public void hostChoiceTest() {
        FormTester formTester = getWicketTester().newFormTester("hostChoice-form");
        formTester.select("choices-host", 1);
    }

    @Test
    public void programChoiceTest() {
        FormTester formTester = getWicketTester().newFormTester("programChoice-form");
        formTester.select("choices-program", 1);
    }
}