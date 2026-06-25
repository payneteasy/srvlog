package com.payneteasy.srvlog.wicket.component.daterange;

import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.util.DateRangeType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTimeField;
import org.apache.wicket.extensions.markup.html.form.datetime.TimeField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

/**
 * Date: 18.02.13 Time: 10:59
 */
public class DateRangePanel extends Panel{
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private WebMarkupContainer holderDateRangeContainer;
    private DateRangeModel dateRangeModel;

    public DateRangePanel(String id) {
        this(id, new DateRangeModel());
    }

    public DateRangePanel(String id, final DateRangeModel dateRangeModel) {
        super(id);
        this.dateRangeModel = dateRangeModel;

        final DropDownChoice<DateRangeType> dateRangeType = new DropDownChoice<>(
                "date-range-type"
                , new PropertyModel<>(dateRangeModel, "dateRangeType")
                , Arrays.asList(DateRangeType.values())
                , new ChoiceRenderer<>("typeDisplayName")
        );
        add(dateRangeType);
        dateRangeType.add(new AjaxFormComponentUpdatingBehavior("change") {
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

        LocalDateTimeField dateFromTimeField = getExactlyDateTimeField("timeFrom-field", dateRangeModel, "exactlyDateFrom");
        holderDateRangeContainer.add(dateFromTimeField);
        LocalDateTimeField dateToTimeField = getExactlyDateTimeField("timeTo-field", dateRangeModel, "exactlyDateTo");
        holderDateRangeContainer.add(dateToTimeField);
    }

    private DateTextField getExactlyDateTextField(String id, final DateRangeModel dateRangeModel, String expression) {
        DateTextField dateTextField = new DateTextField(id, new PropertyModel<>(dateRangeModel, expression), DATE_PATTERN) {
            @Override
            public boolean isVisible() {
                return DateRangeType.EXACTLY_DATE == dateRangeModel.getDateRangeType();
            }
        };
        dateTextField.setRequired(true);
        return dateTextField;
    }

    private LocalDateTimeField getExactlyDateTimeField(String id, final DateRangeModel dateRangeModel, String expression) {
        String title = "exactlyDateFrom".equals(expression)
                ? "Date and time from (DD.MM.YYYY HH:mm)"
                : "Date and time to (DD.MM.YYYY HH:mm)";
        IModel<LocalDateTime> ldtModel = new IModel<LocalDateTime>() {
            @Override
            public LocalDateTime getObject() {
                Date d = new PropertyModel<Date>(dateRangeModel, expression).getObject();
                return d == null ? null : d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            @Override
            public void setObject(LocalDateTime ldt) {
                new PropertyModel<Date>(dateRangeModel, expression)
                    .setObject(ldt == null ? null : Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
            }
        };
        LocalDateTimeField dateTimeField = new LocalDateTimeField(id, ldtModel) {
            @Override
            protected void onInitialize() {
                super.onInitialize();
                get("date").add(AttributeModifier.replace("title", title));
            }
            @Override
            protected TimeField newTimeField(String timeId, IModel<LocalTime> timeModel) {
                return new TimeField(timeId, timeModel) {
                    @Override
                    protected boolean use12HourFormat() {
                        return false;
                    }
                };
            }
            @Override
            public boolean isVisible() {
                return DateRangeType.EXACTLY_TIME == dateRangeModel.getDateRangeType();
            }
        };
        dateTimeField.setRequired(true);
        return dateTimeField;
    }

    private static boolean isVisibleDateField(DateRangeType type) {
        if (DateRangeType.EXACTLY_DATE == type || DateRangeType.EXACTLY_TIME == type) {
            return true;
        }
        return false;
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
