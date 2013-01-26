package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogCount;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.util.DateRangeType;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.json.simple.JSONArray;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date: 16.01.13 Time: 13:03
 */
@Secured("ROLE_ADMIN")
public class LogMainPage extends BasePage {

    @SpringBean
    IIndexerService indexerService;

    public LogMainPage(PageParameters pageParameters) {
        super(pageParameters, LogMainPage.class);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
        add(feedbackPanel);

        final FilterDate filterDate = new FilterDate();
        final DateRange dateRange = filterDate.getDateRange();

        add(new Behavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                response.render(OnDomReadyHeaderItem.forScript(addJs(getJsonArray(dateRange.getFromDate(), dateRange.getToDate()))));
            }
        });

        IModel<List<LogCount>> logCountModel = new LoadableDetachableModel<List<LogCount>>() {
            @Override
            protected List<LogCount> load() {
                try {
                    final DateRange dateRange = filterDate.getDateRange();
                    return indexerService.numberOfSeveritiesByDate(dateRange.getFromDate(), dateRange.getToDate());
                } catch (IndexerServiceException e) {
                    error("Error while retrieving log data: " + e.getMessage());
                    return Collections.emptyList();
                }
            }
        };
        WebMarkupContainer listHolderContainer = new WebMarkupContainer("list-severity-holder");
        listHolderContainer.setOutputMarkupId(true);
        add(listHolderContainer);

        ListView<LogCount> listView = new ListView<LogCount>("list-severity", logCountModel) {
            @Override
            protected void populateItem(ListItem<LogCount> item) {
                LogCount logCount = item.getModelObject();
                Label labelName = new Label("name", logCount.getName());
                LogDataTableUtil.setHighlightCssClassBySeverity(logCount.getName(), labelName);
                item.add(labelName);
                item.add(new Label("count", logCount.getCount()));
            }
        };
        listHolderContainer.add(listView);


        WebMarkupContainer buttonHolderContainer = new WebMarkupContainer("button-group-holder");
        buttonHolderContainer.setOutputMarkupId(true);
        add(buttonHolderContainer);
        buttonHolderContainer.add(addButtonGroup(DateRangeType.THIS_WEEK, filterDate, listHolderContainer, buttonHolderContainer));
        buttonHolderContainer.add(addButtonGroup(DateRangeType.LAST_WEEK, filterDate, listHolderContainer, buttonHolderContainer));
        buttonHolderContainer.add(addButtonGroup(DateRangeType.THIS_MONTH, filterDate, listHolderContainer, buttonHolderContainer));
        buttonHolderContainer.add(addButtonGroup(DateRangeType.LAST_MONTH, filterDate, listHolderContainer, buttonHolderContainer));
    }

    private JSONArray getJsonArray(Date dateFrom, Date dateTo) {
        Map<Date, Long> numberOfLogsByDate = null;
        try {
            numberOfLogsByDate = indexerService.numberOfLogsByDate(dateFrom, dateTo);
        } catch (IndexerServiceException e) {
            error("Error while retrieving log data: " + e.getMessage());
        }

        final JSONArray jsonArrayEntry = new JSONArray();
        for (Map.Entry<Date, Long> logEntry : numberOfLogsByDate.entrySet()) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(logEntry.getKey().getTime());
            jsonArray.add(logEntry.getValue());
            jsonArrayEntry.add(jsonArray);
        }
        return jsonArrayEntry;
    }

    private String addJs(JSONArray numberOfLogsByDate) {
        return new StringBuilder().append("drawChart('").append(numberOfLogsByDate.toString()).append("');").toString();
    }

    private AjaxLink addButtonGroup(final DateRangeType type, final FilterDate filterDate, final Component... componentsForUpdate) {

        final AjaxLink<Void> link = new AjaxLink<Void>(type.name()) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                switch (type) {
                    case THIS_WEEK:
                        filterDate.setDateRange(DateRange.thisWeek(), DateRangeType.THIS_WEEK);
                        break;
                    case LAST_WEEK:
                        filterDate.setDateRange(DateRange.lastWeek(), DateRangeType.LAST_WEEK);
                        break;
                    case THIS_MONTH:
                        filterDate.setDateRange(DateRange.thisMonth(), DateRangeType.THIS_MONTH);
                        break;
                    case LAST_MONTH:
                        filterDate.setDateRange(DateRange.lastMonth(), DateRangeType.LAST_MONTH);
                        break;
                }
                DateRange dateRange = filterDate.getDateRange();
                target.appendJavaScript(addJs(getJsonArray(dateRange.getFromDate(), dateRange.getToDate())));
                target.add(componentsForUpdate);
            }
        };
        setActiveButton(link, filterDate, type);
        return link;
    }

    private void setActiveButton(AjaxLink ajaxLink, final FilterDate filterDate, final DateRangeType dateRangeType) {
        ajaxLink.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
               if(dateRangeType == filterDate.getDateRangeType()){
                    return "active";
               }
               return "";
            }
        }, " "));

    }

    private class FilterDate implements Serializable {
        private DateRange dateRange;
        private DateRangeType dateRangeType;

        private FilterDate() {
            this.dateRange = DateRange.thisMonth(); //by default
            this.dateRangeType = DateRangeType.THIS_MONTH;
        }

        public DateRange getDateRange() {
            return dateRange;
        }

        public void setDateRange(DateRange dateRange, DateRangeType dateRangeType) {
            this.dateRange = dateRange;
            this.dateRangeType = dateRangeType;
        }

        public DateRangeType getDateRangeType() {
            return dateRangeType;
        }

        public void setDateRangeType(DateRangeType dateRangeType) {
            this.dateRangeType = dateRangeType;
        }
    }

}
