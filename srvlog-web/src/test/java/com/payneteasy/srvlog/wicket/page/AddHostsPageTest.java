package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.service.ILogCollector;
import junit.framework.Assert;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 29.01.13 Time: 20:21
 */
public class AddHostsPageTest extends AbstractWicketTester{
    private ILogCollector logCollector;
    private final static String HOST_NAME_1="hostName1";
    private final static String HOST_IP_1="123.123.123.12";
    private final static String HOST_NAME_2="hostName2";
    private final static String HOST_IP_2="12.12.12.21";
    @Override
    protected void setupTest() {
      logCollector = EasyMock.createMock(ILogCollector.class);
      addBean("logCollector", logCollector);
    }

    @Test
    public void testFormPage(){
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        logCollector.saveHosts(getHosts());

        EasyMock.replay(logCollector);

        wicketTester.startPage(AddHostsPage.class);

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.setValue("hosts", getHostsValue());
        formTester.submit("button");

        AddHostsPage.FormModel formModel = (AddHostsPage.FormModel)formTester.getForm().getModelObject();
        Assert.assertEquals(getHostsValue(), formModel.getHosts()); ;

        wicketTester.assertInfoMessages(new ResourceModel("addHost.info").getObject());

        EasyMock.verify(logCollector);
    }

    @Test
    public void testErrorFormPage(){
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.hasUnprocessedLogs()).andReturn(Boolean.TRUE).anyTimes();

        EasyMock.replay(logCollector);

        wicketTester.startPage(AddHostsPage.class);

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.setValue("hosts", "sdfsdfdfsdf");
        formTester.submit("button");

        wicketTester.assertErrorMessages(new ResourceModel("addHost.error").getObject());
        EasyMock.verify(logCollector);
    }

    private List<HostData> getHosts(){
        List<HostData> hostDataList = new ArrayList<HostData>();
        HostData hostData1 = new HostData();
        hostData1.setHostname(HOST_NAME_1);
        hostData1.setIpAddress(HOST_IP_1);
        HostData hostData2 = new HostData();
        hostData2.setHostname(HOST_NAME_2);
        hostData2.setIpAddress(HOST_IP_2);
        return hostDataList;
    }

    private String getHostsValue(){
        StringBuilder sb = new StringBuilder();
        sb.append(HOST_NAME_1);
        sb.append(";");
        sb.append(HOST_IP_1);
        sb.append(",");
        sb.append(HOST_NAME_2);
        sb.append(";");
        sb.append(HOST_IP_2);
        return sb.toString();
    }

}
