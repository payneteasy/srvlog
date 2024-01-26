package com.payneteasy.srvlog.wicket.component.daterange;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

/**
 * Date: 18.02.13 Time: 11:28
 */
public class TestDateRangePage extends WebPage{

    public TestDateRangePage() {
        DateRangePanel.DateRangeModel dateRangeModel = new DateRangePanel.DateRangeModel();
        Form<DateRangePanel.DateRangeModel> form = new Form<>("form", new Model<>(dateRangeModel)) {
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
