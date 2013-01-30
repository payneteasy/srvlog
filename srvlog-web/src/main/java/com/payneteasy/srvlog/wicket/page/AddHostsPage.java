package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 29.01.13 Time: 20:13
 */
public class AddHostsPage extends BasePage{

    @SpringBean
    private ILogCollector logCollector;

    public AddHostsPage(PageParameters pageParameters) {
        super(pageParameters, AddHostsPage.class);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
        add(feedbackPanel);

        final Form<FormModel> form = new Form<FormModel>("form", new CompoundPropertyModel<FormModel>(new FormModel()));
        add(form);

        RequiredTextField<String> hostTextField = new RequiredTextField<String>("hosts");
        form.add(hostTextField);

        form.add(new Button("button"){
            @Override
            public void onSubmit() {
                FormModel formModel = form.getModelObject();
                if (parseHosts(formModel.getHosts())){
                    info(new ResourceModel("addHost.info").getObject());
                }else{
                    error(new ResourceModel("addHost.error").getObject());
                }
            }
        });
    }

    public class FormModel implements Serializable{
        private String hosts;

        public String getHosts() {
            return hosts;
        }

        public void setHosts(String hosts) {
            this.hosts = hosts;
        }
    }

    private boolean parseHosts(String value){
        String[] hostsDataArray = value.split(",");
        if(hostsDataArray.length <= 0){
            return false;
        }
        List<HostData> hostDataList = new ArrayList<HostData>();
        for (String hostDataString : hostsDataArray) {
            String[] host = hostDataString.split(";");
            if(host.length!=2){
               return false;
            }

            HostData hostData = new HostData();
            hostData.setHostname(host[0]);
            hostData.setIpAddress(host[1]);
        }
        logCollector.saveHosts(hostDataList);
        return true;
    }
}
