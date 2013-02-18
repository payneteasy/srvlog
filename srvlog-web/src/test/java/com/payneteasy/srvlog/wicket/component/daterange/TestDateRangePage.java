package com.payneteasy.srvlog.wicket.component.daterange;

import com.payneteasy.srvlog.wicket.page.AddHostsPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 18.02.13 Time: 11:28
 */
public class TestDateRangePage extends WebPage{

    public TestDateRangePage() {
        DateRangePanel.DateRangeModel dateRangeModel = new DateRangePanel.DateRangeModel();
        Form<DateRangePanel.DateRangeModel> form = new Form<DateRangePanel.DateRangeModel>("form", new Model<DateRangePanel.DateRangeModel>(dateRangeModel)){
            @Override
            protected void onSubmit() {

            }
        };

        add(form);
        DateRangePanel dateRangePanel = new DateRangePanel("date-range-panel", dateRangeModel);
        form.add(dateRangePanel);

        form.add(new Button("button"){
            @Override
            public void onSubmit() {

            }
        });

    }

}
