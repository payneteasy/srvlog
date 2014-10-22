package com.payneteasy.srvlog.wicket.page;

import static com.payneteasy.srvlog.adapter.syslog.IpHeader.isContainsIpHeader;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.data.SnortLogData;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.wicket.component.HexViewerPanel;
import static java.lang.String.valueOf;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.time.DateFormatUtils.format;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

/**
 * Page contains processed snort messages.
 *
 * @author imenem
 */
public class SnortLogMonitorPage extends BasePage {

    @SpringBean
    private ILogCollector logCollector;

    public SnortLogMonitorPage(PageParameters pageParameters) {
        super(pageParameters, SnortLogMonitorPage.class);

        StringValue hash = pageParameters.get("hash");

        if (hash.isEmpty()) {
            throw new RuntimeException("Parameter 'hash' must be specified.");
        }

        addLogs(hash.toString());
        addSnortLogs(hash.toString());
    }

    private void addLogs(String hash) {
        final List<LogData> logs = logCollector.getLogsByHash(hash);

        add(new ListView<LogData>("logs", logs) {

            @Override
            protected void populateItem(ListItem<LogData> li) {
                LogData log = li.getModelObject();

                String logLevel = LogLevel.forValue(log.getSeverity());
                li.add(new Label("log-date", format(log.getDate().getTime(), "yyyy-MM-dd HH:mm:ss")));
                li.add(new Label("log-severity", logLevel));
                li.add(new Label("log-facility", LogFacility.forValue(log.getFacility())));
                li.add(new Label("log-host", log.getHost()));
                li.add(new Label("log-program", log.getProgram()==null? "-":log.getProgram()));
                li.add(new Label("log-message", log.getMessage()));
            }
        });

        add(new WebMarkupContainer("no-logs"){
            @Override
            public boolean isVisible() {
                return logs.isEmpty();
            }
        });
    }

    private void addSnortLogs(String hash) {
        final List<SnortLogData> snortLogs = logCollector.getSnortLogsByHash(hash);

        add(new ListView<SnortLogData>("snort-logs", snortLogs) {

            @Override
            protected void populateItem(ListItem<SnortLogData> li) {
                SnortLogData snortLogData = li.getModelObject();

                li.add(new Label("date", snortLogData.getDate().toString()));
                li.add(new Label("priority", valueOf(snortLogData.getPriority())));
                li.add(new Label("classification", snortLogData.getClassification()));
                li.add(new Label("alert-cause", snortLogData.getAlertCause()));
                li.add(new Label("ip-header", getHeaderInfo(snortLogData)));
                li.add(new HexViewerPanel("payload", snortLogData.getPayload()));

                addHttpHeaders(li, snortLogData);
            }

            protected void addHttpHeaders(ListItem<SnortLogData> li, SnortLogData data) {
                if (!isEmpty(data.getHost())) {
                    li.add(new Label("host", "Host: " + data.getHost()));
                }
                else {
                    li.add(new Label("host", ""));
                }

                if (!isEmpty(data.getxForwardedFor())) {
                    li.add(new Label("x-forwarded-for", "X-Forwarded-For: " + data.getxForwardedFor()  ));
                }
                else {
                    li.add(new Label("x-forwarded-for", ""));
                }

                if (!isEmpty(data.getxRealIp())) {
                    li.add(new Label("x-real-ip", "X-Real-IP: " + data.getxRealIp()));
                }
                else {
                    li.add(new Label("x-real-ip", ""));
                }
            }

            private String getHeaderInfo(SnortLogData data) {
                if (!isContainsIpHeader(data)) {
                    return "";
                }

                return data.getProtocolAlias() + " " +
                    data.getSourceIp() + ":" + data.getSourcePort() + " -> " +
                    data.getDestinationIp() + ":" + data.getDestinationPort();
            }

        });

        add(new WebMarkupContainer("no-snort-logs"){
            @Override
            public boolean isVisible() {
                return snortLogs.isEmpty();
            }
        });
    }
}
