package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.adapter.syslog.OssecSnortMessage;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.data.SnortLogData;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.wicket.component.HexViewerPanel;
import com.payneteasy.srvlog.wicket.component.JsonViewerPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

import static com.payneteasy.srvlog.adapter.syslog.IpHeader.isContainsIpHeader;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

/**
 * Page contains processed snort messages.
 *
 * @author imenem
 */
public class SnortLogMonitorPage extends BasePage {

    private static final Logger LOG = LoggerFactory.getLogger(SnortLogMonitorPage.class);

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

        add(new ListView<>("logs", logs) {

            @Override
            protected void populateItem(ListItem<LogData> li) {
                LogData log = li.getModelObject();

                String logLevel = LogLevel.forValue(log.getSeverity());
                li.add(new Label("log-date", format(log.getDate().getTime(), "yyyy-MM-dd HH:mm:ss")));
                li.add(new Label("log-severity", logLevel));
                li.add(new Label("log-facility", LogFacility.forValue(log.getFacility())));
                li.add(new Label("log-host", log.getHost()));
                li.add(new Label("log-program", log.getProgram() == null ? "-" : log.getProgram()));
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
        final List<SnortLogData> snortLogs = logCollector.getOssecLogsByHash(hash)
            .stream()
            .map(OssecSnortMessage::createOssecSnortMessage)
            .map(logCollector::getSnortLogs)
            .flatMap(List::stream)
            .collect(toList());

        snortLogs.sort(Comparator.comparing(SnortLogData::getDate));

        add(new ListView<SnortLogData>("snort-logs", snortLogs) {

            @Override
            protected void populateItem(ListItem<SnortLogData> li) {
                SnortLogData snortLogData = li.getModelObject();

                li.add(new Label("date", snortLogData.getDate().toString()));
                li.add(new Label("priority", valueOf(snortLogData.getPriority())));
                li.add(new ExternalLink(
                    "classification-url",
                    snortLogData.getSignature().getDescriptionUrl(),
                    snortLogData.getClassification())
                );
                li.add(new Label("alert-cause", snortLogData.getAlertCause()));
                li.add(new Label("ip-header", getHeaderInfo(snortLogData)));

                LOG.debug("Program = {}, classification = {}, id = {}", snortLogData.getProgram(), snortLogData.getClassification(), snortLogData.getId());
                
                Panel payload = "suricata".equals(snortLogData.getProgram())
                        ? new JsonViewerPanel("payload", snortLogData.getPayload())
                        : new HexViewerPanel("payload", snortLogData.getPayload());
                li.add(payload);

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
