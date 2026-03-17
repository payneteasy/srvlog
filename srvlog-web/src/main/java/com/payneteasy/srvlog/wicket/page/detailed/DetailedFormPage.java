package com.payneteasy.srvlog.wicket.page.detailed;

import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.wicket.component.FlatpickrBehavior;
import org.apache.wicket.Page;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 18.02.13 Time: 13:19
 */
public class DetailedFormPage extends DetailedLogsPage{
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String FLATPICKR_DATE_FORMAT = "d.m.Y"; // Flatpickr uses different format syntax
    private FilterDetailedModel filterDetailedModel;
    private Form form;
    private DateTextField dateTextField;
    
    public DetailedFormPage(PageParameters parameters, Class<? extends Page> pageClass){
        super(parameters, pageClass);

        filterDetailedModel = new FilterDetailedModel();

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedBack-panel");
        add(feedbackPanel);

        form = new Form<Void>("form");
        add(form);

        dateTextField = new DateTextField("date-field", new PropertyModel<>(filterDetailedModel, "date"), new PatternDateConverter(DATE_PATTERN, false));
        // Use FlatpickrBehavior for consistent date picker across the application
        dateTextField.add(new FlatpickrBehavior(FLATPICKR_DATE_FORMAT));
        dateTextField.setRequired(true);
        form.add(dateTextField);

        form.add(new Button("button"){
            @Override
            public void onSubmit() {}
        });
    }

    public FilterDetailedModel getFilterDetailedModel() {
        return filterDetailedModel;
    }

    public Form getForm() {
        return form;
    }

    protected static class FilterDetailedModel implements Serializable {
        private Date date;

        public FilterDetailedModel() {
            DateRange today = DateRange.today();
            this.date = today.getFromDate();
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

}
