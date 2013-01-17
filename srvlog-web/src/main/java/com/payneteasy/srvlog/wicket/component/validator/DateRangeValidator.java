package com.payneteasy.srvlog.wicket.component.validator;

import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import java.util.Date;

/**
 * Date: 16.01.13 Time: 23:21
 */
public class DateRangeValidator extends AbstractFormValidator {

    public DateRangeValidator(DateTimeField dateTimeField, String keyPrefix) {
        this.dateTimeField = dateTimeField;
        this.keyPrefix = keyPrefix;
        formComponent = new FormComponent[] {this.dateTimeField};
    }

    public DateRangeValidator(DateTextField dateTextField, String keyPrefix) {
        this.dateTextField = dateTextField;
        this.keyPrefix = keyPrefix;
        formComponent = new FormComponent[] {this.dateTextField};
    }

    @Override
    public FormComponent<?>[] getDependentFormComponents() {
        return new FormComponent<?>[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validate(Form<?> form) {
        //check date time field
        if (dateTimeField != null && dateTimeField.isVisible()) {
            if(dateTimeField.getDate() == null){
                error(keyPrefix, "DateRequired");
            }
            if (dateTimeField.getHours() == null) {
                error(keyPrefix, "HoursRequire");
            }

            if(dateTimeField.getMinutes() == null){
                error(keyPrefix, "MinutesRequired");
            }
        }

        //check date text field
        if(dateTextField != null && dateTextField.isVisible()){
            if(dateTextField.getConvertedInput() == null){
                error(keyPrefix, "DateRequired");
            }
        }
    }

    private void error(String keyPrefix , String errorKey) {
        formComponent[0].error(new ResourceModel(new StringBuilder().append(keyPrefix).append(".").append(errorKey).toString()).getObject());
    }

    private String keyPrefix;
    private DateTimeField dateTimeField;
    private DateTextField dateTextField;
    private FormComponent<?>[] formComponent;
}
