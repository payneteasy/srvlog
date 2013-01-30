package com.payneteasy.srvlog.wicket.page;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;

/**
 * Date: 29.01.13 Time: 20:13
 */
public class AddHostsPage extends BasePage{

    public AddHostsPage(PageParameters pageParameters) {
        super(pageParameters, AddHostsPage.class);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
        add(feedbackPanel);

        Form<FormModel> form = new Form<FormModel>("form", new CompoundPropertyModel<FormModel>(new FormModel()));
        add(form);

        RequiredTextField<String> hostTextField = new RequiredTextField<String>("hosts");
        form.add(hostTextField);

        form.add(new Button("button"){
            @Override
            public void onSubmit() {

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
}
