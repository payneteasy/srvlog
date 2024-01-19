package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.util.List;

@Secured("ROLE_ADMIN")
public class TerminalPage extends BasePage {

    @SpringBean
    private ILogBroadcastingService logBroadcastingService;

    public TerminalPage(PageParameters pageParameters) {

        super(pageParameters, TerminalPage.class);

        final TerminalFilterModel filterModel = new TerminalFilterModel();

        Form<TerminalFilterModel> hostChoiceForm = new Form<>("hostChoice-form");
        add(hostChoiceForm);

        List<String> hosts = logBroadcastingService.getHostNameList();

        DropDownChoice<String> hostChoices = new DropDownChoice<>(
                "choices-host",
                new PropertyModel<>(filterModel, "hostName"),
                new LoadableDetachableModel<List<String>>() {
                    @Override
                    protected List<String> load() {
                        return hosts;
                    }
                },
                new ChoiceRenderer<>()
        );

        hostChoices.setNullValid(true);
        hostChoiceForm.add(hostChoices);

        Form<TerminalFilterModel> programChoiceForm = new Form<>("programChoice-form");
        add(programChoiceForm);

        List<String> programs = logBroadcastingService.getProgramNameList();

        DropDownChoice<String> programChoices = new DropDownChoice<>(
                "choices-program",
                new PropertyModel<>(filterModel, "programName"),
                new LoadableDetachableModel<List<String>>() {
                    @Override
                    protected List<String> load() {
                        return programs;
                    }
                },
                new ChoiceRenderer<>()
        );

        programChoices.setNullValid(true);
        programChoiceForm.add(programChoices);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(new JavaScriptContentHeaderItem("let applicationContextPath = '"
                + WebApplication.get().getServletContext().getContextPath() +  "';", null));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/hterm_all-1.91.js")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/terminal-1.0.0.js")));
    }

    public static class TerminalFilterModel implements Serializable {

        private String hostName;
        private String programName;

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }
    }
}
