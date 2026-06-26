package com.payneteasy.srvlog.wicket.component.validator;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTimeField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.ResourceModel;

/**
 * Date: 16.01.13 Time: 23:21
 */
public class DateRangeValidator extends AbstractFormValidator {

    public DateRangeValidator(LocalDateTimeField dateTimeField, String keyPrefix) {
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
        return new FormComponent<?>[0];
    }

    @Override
    public void validate(Form<?> form) {
        if (dateTimeField != null && dateTimeField.isVisible()) {
            if (dateTimeField.getConvertedInput() == null) {
                error(keyPrefix, "DateRequired");
            }
        }

        if (dateTextField != null && dateTextField.isVisible()) {
            if (dateTextField.getConvertedInput() == null) {
                error(keyPrefix, "DateRequired");
            }
        }
    }

    private void error(String keyPrefix, String errorKey) {
        formComponent[0].error(new ResourceModel(keyPrefix + "." + errorKey).getObject());
    }

    private String keyPrefix;
    private LocalDateTimeField dateTimeField;
    private DateTextField dateTextField;
    private FormComponent<?>[] formComponent;
}
