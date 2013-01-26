package com.payneteasy.srvlog.utils;

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
                        return "LEVEL-WARN";
                    }
                }));
                break;
            case ERROR:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "LEVEL-ERROR";
                    }
                }));
                break;
            case EMERGENCY:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "LEVEL-EMERGENCY";
                    }
                }));
                break;
            case CRITICAL:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "LEVEL-CRITICAL";
                    }
                }));
                break;
            case ALERT:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "LEVEL-ALERT";
                    }
                }));
                break;
            case INFO:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "LEVEL-INFO";
                    }
                }));
                break;
            case NOTICE:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "LEVEL-NOTICE";
                    }
                }));
                break;
            case DEBUG:
                component.add(new AttributeAppender("class", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return "LEVEL-DEBUG";
                    }
                }));
                break;
        }
    }
}
