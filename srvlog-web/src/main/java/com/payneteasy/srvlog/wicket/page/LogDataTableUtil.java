package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogLevel;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Date: 15.01.13 Time: 17:38
 */
public class LogDataTableUtil {

    public static void setHighlightCssClassBySeverity(String logLevel, Component component) {
        switch (LogLevel.valueOf(logLevel)) {
            case WARN:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "warning";
                    }
                }));
                break;
            case ERROR:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "error";
                    }
                }));
                break;
            case EMERGENCY:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "main-emergency";
                    }
                }));
                break;
            case CRITICAL:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "main-critical";
                    }
                }));
                break;
            case ALERT:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "warning";
                    }
                }));
                break;
            case INFO:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "info";
                    }
                }));
                break;
        }
    }
}
