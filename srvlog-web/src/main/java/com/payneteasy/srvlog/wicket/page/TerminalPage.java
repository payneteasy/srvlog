package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import com.payneteasy.srvlog.wicket.component.MultiSelectDropdown;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

@Secured("ROLE_ADMIN")
public class TerminalPage extends BasePage {

    @SpringBean
    private ILogBroadcastingService logBroadcastingService;

    public TerminalPage(PageParameters pageParameters) {
        super(pageParameters, TerminalPage.class);
        addMultiSelectDropdownEntity("choices-host", logBroadcastingService.getHostNameList());
        addMultiSelectDropdownEntity("choices-program", logBroadcastingService.getProgramNameList());
    }

    private void addMultiSelectDropdownEntity(String id, List<String> choices) {

        MultiSelectDropdown<String> multiSelectDropdown = new MultiSelectDropdown<>(
                id,
                choices,
                new ChoiceRenderer<>()
        );
        add(multiSelectDropdown);
    }

    @Override
    public void renderHead(IHeaderResponse response) {

        super.renderHead(response);

        response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "css/multi-select-dropdown.css")));

        response.render(new JavaScriptContentHeaderItem("let applicationContextPath = '"
                + WebApplication.get().getServletContext().getContextPath() +  "';", null));

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/hterm_all-1.91.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/terminal-1.0.0.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/multi-select-dropdown.js")));

        response.render(OnLoadHeaderItem.forScript("document.querySelectorAll('[data-multi-select]').forEach(select => new MultiSelect(select));"));
    }
}
