package com.payneteasy.srvlog.wicket.component.repeater;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.HashMap;
import java.util.Map;

import static com.payneteasy.srvlog.wicket.component.repeater.LogDataTableUtil.setHighlightCssClass;

/**
 * Date: 15.01.13 Time: 11:35
 */
public abstract class AbstractLogDataTableViewPanel<T extends LogData> extends Panel{

    public AbstractLogDataTableViewPanel(String id) {
        super(id);

        IPagingLister<T> lister = getPageLister();

        setPositionOptionsFromRow(0);
        setPositionOptionsItemPerPage(25);
        pagingController = new LightweightPagingController<T>(lister, positionOptions);

        ListView<T> listView = new ListView<T>("list-log-data", pagingController.getListModel()) {
            @Override
            protected void populateItem(ListItem<T> item) {
                LogData logData = item.getModelObject();
                String logLevel = LogLevel.forValue(logData.getSeverity());
                item.add(new Label("log-date", DateFormatUtils.SMTP_DATETIME_FORMAT.format(logData.getDate().getTime())));
                item.add(new Label("log-severity", logLevel));
                item.add(new Label("log-facility", LogFacility.forValue(logData.getFacility())));
                item.add(new Label("log-host", logData.getHost()));
                item.add(new Label("log-message", logData.getMessage()));
                setHighlightCssClass(logLevel, item);
            }
        };

        add(listView);

        add(new LightweightPagingNavigator("pager", pagingController.getNavigable()));

    }

    protected void setPositionOptionsFromRow(int fromRow){
        positionOptions.setFromRow(fromRow);
    }

    protected void setPositionOptionsItemPerPage(int itemPerPage){
        positionOptions.setItemsPerPage(itemPerPage);
    }

    protected abstract IPagingLister<T> getPageLister();
    private PositionOptions positionOptions = new PositionOptions();
    private IPagingController<T> pagingController;
}
