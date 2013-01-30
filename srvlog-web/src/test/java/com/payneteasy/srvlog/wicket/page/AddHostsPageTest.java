package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogCollector;
import junit.framework.Assert;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.easymock.EasyMock;
import org.junit.Test;

/**
 * Date: 29.01.13 Time: 20:21
 */
public class AddHostsPageTest extends AbstractWicketTester{
    private ILogCollector logCollector;
    @Override
    protected void setupTest() {
      logCollector = EasyMock.createMock(ILogCollector.class);
      addBean("logCollector", logCollector);
    }

    @Test
    public void testFormPage(){
        WicketTester wicketTester = getWicketTester();

        EasyMock.expect(logCollector.getNumberUnprocessedHosts()).andReturn(6L);

        EasyMock.replay(logCollector);

        wicketTester.startPage(AddHostsPage.class);

        FormTester formTester = wicketTester.newFormTester("form");
        formTester.setValue("hosts", "test-hosts");
        formTester.submit("button");

        AddHostsPage.FormModel formModel = (AddHostsPage.FormModel)formTester.getForm().getModelObject();
        Assert.assertEquals("test-hosts", formModel.getHosts()); ;

        EasyMock.verify(logCollector);
    }
}
