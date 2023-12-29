package com.payneteasy.srvlog.utils;

import com.payneteasy.srvlog.data.LogLevel;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;

/**
 * Date: 15.01.13 Time: 17:38
 */
public class LogDataTableUtil {

    public static void setHighlightCssClassBySeverity(String logLevel, Component component) {
        switch (LogLevel.valueOf(logLevel)) {
            case WARN:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-WARN"));
                break;
            case ERROR:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-ERROR"));
                break;
            case EMERGENCY:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-EMERGENCY"));
                break;
            case CRITICAL:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-CRITICAL"));
                break;
            case ALERT:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-ALERT"));
                break;
            case INFO:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-INFO"));
                break;
            case NOTICE:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-NOTICE"));
                break;
            case DEBUG:
                component.add(new AttributeAppender("class", (IModel<Object>) () -> "LEVEL-DEBUG"));
                break;
        }
    }
}
