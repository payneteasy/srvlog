package com.payneteasy.srvlog.wicket.page.detailed;

import org.apache.wicket.Page;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
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
    private FilterDetailedModel filterDetailedModel;
    private Form form;
    public DetailedFormPage(PageParameters parameters, Class<? extends Page> pageClass){
        super(parameters, pageClass);

        filterDetailedModel = new FilterDetailedModel();

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedBack-panel");
        add(feedbackPanel);

        form = new Form<Void>("form");
        add(form);

        DateTextField dateTextField = new DateTextField("date-field", new PropertyModel<Date>(filterDetailedModel, "date"), new PatternDateConverter(DATE_PATTERN, false));
        dateTextField.add(new DatePicker());
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

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

}
