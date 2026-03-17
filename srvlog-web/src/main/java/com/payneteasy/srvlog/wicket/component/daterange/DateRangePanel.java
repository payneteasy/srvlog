package com.payneteasy.srvlog.wicket.component.daterange;

import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.util.DateRangeType;
import com.payneteasy.srvlog.wicket.component.FlatpickrBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Date: 18.02.13 Time: 10:59
 */
public class DateRangePanel extends Panel{
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";
    
    // Flatpickr format patterns (different from Java patterns)
    private static final String FLATPICKR_DATE_FORMAT = "d.m.Y";
    private static final String FLATPICKR_DATETIME_FORMAT = "d.m.Y H:i";
    private WebMarkupContainer holderDateRangeContainer;
    private DateRangeModel dateRangeModel;
    private DropDownChoice<DateRangeType> dateRangeTypeChoice;

    public DateRangePanel(String id) {
        this(id, new DateRangeModel());
    }

    public DateRangePanel(String id, final DateRangeModel dateRangeModel) {
        super(id);
        this.dateRangeModel = dateRangeModel;

        dateRangeTypeChoice = new DropDownChoice<>(
                "date-range-type"
                , new PropertyModel<>(dateRangeModel, "dateRangeType")
                , Arrays.asList(DateRangeType.values())
                , new ChoiceRenderer<>("typeDisplayName")
        );
        dateRangeTypeChoice.setOutputMarkupId(true); // Enable Ajax updates for the dropdown
        add(dateRangeTypeChoice);
        dateRangeTypeChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(holderDateRangeContainer);
            }
        });

        holderDateRangeContainer = new WebMarkupContainer("holder-exactly-dateRange") {
            @Override
            public boolean isVisible() {
                return isVisibleDateField(dateRangeModel.getDateRangeType());
            }
        };
        holderDateRangeContainer.setOutputMarkupPlaceholderTag(true);
        add(holderDateRangeContainer);

        DateTextField dateFromTextField = getExactlyDateTextField("dateFrom-field", dateRangeModel, "exactlyDateFrom");
        holderDateRangeContainer.add(dateFromTextField);
        DateTextField dateToTextField = getExactlyDateTextField("dateTo-field", dateRangeModel, "exactlyDateTo");
        holderDateRangeContainer.add(dateToTextField);

        DateTextField dateFromTimeField = getExactlyDateTimeField("timeFrom-field", dateRangeModel, "exactlyDateFrom");
        holderDateRangeContainer.add(dateFromTimeField);
        DateTextField dateToTimeField = getExactlyDateTimeField("timeTo-field", dateRangeModel, "exactlyDateTo");
        holderDateRangeContainer.add(dateToTimeField);
    }

    private Date getFromDate(){
        return dateRangeModel.getDateRange().getFromDate();
    }
    private Date getToDate(){
        return dateRangeModel.getDateRange().getToDate();
    }

    private DateTextField getExactlyDateTextField(String id, final DateRangeModel dateRangeModel, String expression) {
        DateTextField dateTextField = new DateTextField(id, new PropertyModel<>(dateRangeModel, expression), new PatternDateConverter(DATE_PATTERN, false)) {
            @Override
            public boolean isVisible() {
                return DateRangeType.EXACTLY_DATE == dateRangeModel.getDateRangeType();
            }
        };
        // Use Flatpickr instead of YUI DatePicker
        dateTextField.add(new FlatpickrBehavior(FLATPICKR_DATE_FORMAT));
        dateTextField.setRequired(true);
        return dateTextField;
    }

    private DateTextField getExactlyDateTimeField(String id, final DateRangeModel dateRangeModel, String expression) {
        DateTextField dateTimeField = new DateTextField(id, new PropertyModel<>(dateRangeModel, expression), new PatternDateConverter(DATE_TIME_PATTERN, false)) {
            @Override
            public boolean isVisible() {
                return DateRangeType.EXACTLY_TIME == dateRangeModel.getDateRangeType();
            }
        };
        // Use Flatpickr with time enabled
        dateTimeField.add(new FlatpickrBehavior(FLATPICKR_DATETIME_FORMAT, true, true));
        dateTimeField.setRequired(true);
        return dateTimeField;
    }
    private static boolean isVisibleDateField(DateRangeType type) {
        if (DateRangeType.EXACTLY_DATE == type || DateRangeType.EXACTLY_TIME == type) {
            return true;
        }
        return false;
    }
    
    /**
     * Get the date range type dropdown for external Ajax updates
     */
    public DropDownChoice<DateRangeType> getDateRangeTypeChoice() {
        return dateRangeTypeChoice;
    }

    public static class DateRangeModel implements Serializable{
        private DateRange dateRange;
        private DateRangeType dateRangeType;
        private Date exactlyDateFrom;
        private Date exactlyDateTo;

        public DateRangeModel() {
            this.dateRange = DateRange.today();
            this.dateRangeType = DateRangeType.TODAY;
        }

        public void setDateRangeType(DateRangeType dateRangeType) {
            this.dateRangeType = dateRangeType;
            setDateRange();
        }

        public DateRangeType getDateRangeType() {
            return dateRangeType;
        }

        public Date getExactlyDateFrom() {
            return exactlyDateFrom;
        }

        public void setExactlyDateFrom(Date exactlyDateFrom) {
            this.exactlyDateFrom = exactlyDateFrom;
        }

        public Date getExactlyDateTo() {
            return exactlyDateTo;
        }

        public void setExactlyDateTo(Date exactlyDateTo) {
            this.exactlyDateTo = exactlyDateTo;
        }

        public DateRange getDateRange() {
            if (isVisibleDateField(this.dateRangeType)) {
                dateRange = new DateRange(exactlyDateFrom, exactlyDateTo);
            }
            return dateRange;
        }

        private void setDateRange() {
            switch (this.dateRangeType) {
                case TODAY:
                    dateRange = DateRange.today();
                    break;
                case YESTERDAY:
                    dateRange = DateRange.yesterday();
                    break;
                case THIS_WEEK:
                    dateRange = DateRange.thisWeek();
                    break;
                case LAST_WEEK:
                    dateRange = DateRange.lastWeek();
                    break;
                case THIS_MONTH:
                    dateRange = DateRange.thisMonth();
                    break;
                case LAST_MONTH:
                    dateRange = DateRange.lastMonth();
                    break;
                case EXACTLY_DATE:
                    break;
                case EXACTLY_TIME:
                    break;
                default:
                    throw new IllegalArgumentException("Unknown date range type: " + this.dateRangeType);
            }
        }
    }
}
