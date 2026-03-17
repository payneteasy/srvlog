package com.payneteasy.srvlog.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Behavior that adds Flatpickr date picker functionality to a component.
 * Flatpickr is a lightweight, powerful JavaScript date picker.
 * 
 * Usage:
 * <pre>
 * DateTextField dateField = new DateTextField("date", model);
 * dateField.setOutputMarkupId(true);
 * dateField.add(new FlatpickrBehavior("d.m.Y"));
 * </pre>
 */
public class FlatpickrBehavior extends Behavior {
    private static final long serialVersionUID = 1L;

    private static final CssResourceReference FLATPICKR_CSS =
        new CssResourceReference(FlatpickrBehavior.class, "flatpickr.min.css");
    private static final JavaScriptResourceReference FLATPICKR_JS =
        new JavaScriptResourceReference(FlatpickrBehavior.class, "flatpickr.min.js");
    
    private final String dateFormat;
    private final boolean enableTime;
    private final boolean time24hr;
    
    /**
     * Create Flatpickr behavior with custom date format.
     * 
     * @param dateFormat The date format (e.g., "d.m.Y" for day.month.year)
     */
    public FlatpickrBehavior(String dateFormat) {
        this(dateFormat, false, true);
    }
    
    /**
     * Create Flatpickr behavior with custom date format and time options.
     * 
     * @param dateFormat The date format (e.g., "d.m.Y H:i" for day.month.year hour:minute)
     * @param enableTime Enable time selection
     * @param time24hr Use 24-hour time format
     */
    public FlatpickrBehavior(String dateFormat, boolean enableTime, boolean time24hr) {
        this.dateFormat = dateFormat;
        this.enableTime = enableTime;
        this.time24hr = time24hr;
    }
    
    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        
        response.render(CssHeaderItem.forReference(FLATPICKR_CSS));
        response.render(JavaScriptHeaderItem.forReference(FLATPICKR_JS));
        
        // Initialize Flatpickr on the component
        String initScript = buildInitScript(component);
        response.render(OnDomReadyHeaderItem.forScript(initScript));
    }
    
    private String buildInitScript(Component component) {
        StringBuilder script = new StringBuilder();
        script.append("if (typeof flatpickr !== 'undefined') { ");
        script.append("flatpickr('#").append(component.getMarkupId()).append("', { ");
        script.append("dateFormat: '").append(dateFormat).append("', ");
        script.append("allowInput: true");
        
        if (enableTime) {
            script.append(", enableTime: true");
            script.append(", time_24hr: ").append(time24hr);
        }
        
        script.append(" }); }");
        return script.toString();
    }
    
    @Override
    public void bind(Component component) {
        super.bind(component);
        component.setOutputMarkupId(true);
    }
}
