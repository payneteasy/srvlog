package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.IndexerServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date: 14.01.13 Time: 16:48
 */
@Transactional
@Service
public class SphinxIndexerService implements IIndexerService{

    private static final Logger LOG = LoggerFactory.getLogger(SphinxIndexerService.class);

    @Value( "${host}" )
    private String host;

    @Value( "${port}" )
    private int port;

    @Value( "${connectTimeout}" )
    private int connectTimeout;

    @Override
    public List<Long> search(Date from, Date to, List<Integer> facilities, List<Integer> severities, List<Integer> hosts, String pattern, Integer offset, Integer limit) throws IndexerServiceException {
        SphinxClient sphinxClient = createSphinxClient();


        setMatchMode(sphinxClient);

        setDate(from, to, sphinxClient);


        setAttributeFilter(facilities, sphinxClient, "facility");

        setAttributeFilter(hosts, sphinxClient, "host_id");

        setAttributeFilter(severities, sphinxClient, "severity");

        setLimits(offset, limit, sphinxClient);


        if (pattern == null) {
            pattern = "";
        }

        SphinxResult result = querySphinx(pattern, sphinxClient);
        if (errorOccuredWhileQuerying(sphinxClient, result)) return Collections.emptyList();
        reportWarningsIfAny(sphinxClient);

        List<Long> logIds = new LinkedList<Long>();
        for (SphinxMatch sm: result.matches) {
            logIds.add(sm.docId);
        }
       return logIds;
   }

    private void setLimits(Integer offset, Integer limit, SphinxClient sphinxClient) throws IndexerServiceException {
        if (offset != null && limit != null) {
            try {
                sphinxClient.SetLimits(offset, limit);

            } catch (SphinxException e) {
                LOG.error("While trying to set limits", e);
                throw new IndexerServiceException("While trying to set limits", e);
            }
        }
    }

    private SphinxClient createSphinxClient() {
        SphinxClient sphinxClient = new SphinxClient(host, port);
        sphinxClient.SetConnectTimeout(connectTimeout);
        return sphinxClient;
    }

    private void reportWarningsIfAny(SphinxClient sphinxClient) {
        if (StringUtils.isNotEmpty(sphinxClient.GetLastWarning())) {
            LOG.warn("From Sphinx Daemon: " + sphinxClient.GetLastWarning());
        }
    }

    private boolean errorOccuredWhileQuerying(SphinxClient sphinxClient, SphinxResult result) throws IndexerServiceException {
        if (result == null || result.matches.length == 0) {
            if (StringUtils.isNotEmpty(sphinxClient.GetLastError())) {
                LOG.error("While sending query to Sphinx Daemon: " + sphinxClient.GetLastError());
                throw new IndexerServiceException("While sending query to Sphinx Daemon: " + sphinxClient.GetLastError());
            }
            return true;
        }
        return false;
    }

    private SphinxResult querySphinx(String pattern, SphinxClient sphinxClient) throws IndexerServiceException {
        SphinxResult result;
        try {
            result = sphinxClient.Query(pattern);
        } catch (SphinxException e) {
            LOG.error("While sending query to Sphinx Daemon", e);
            throw new IndexerServiceException("While sending query to Sphinx Daemon", e);
        }
        return result;
    }

    private void setDate(Date from, Date to, SphinxClient sphinxClient) throws IndexerServiceException {
        if (from != null && to != null) {
            try {
                sphinxClient.SetFilterRange("log_date", toUnixTime(from), toUnixTime(to), false);
                sphinxClient.SetSortMode(SphinxClient.SPH_SORT_ATTR_DESC, "log_date");
            } catch (SphinxException e) {
                LOG.error("While trying to set date range", e);
                throw new IndexerServiceException("While trying to set date range", e);
            }
        }
    }

    @Override
    public Map<Date, Long> numberOfLogsByDate(Date from, Date to) throws IndexerServiceException{
         return numberOfLogsByDate(from, to, 0, 31);
    }



    @Override
    public Map<LogLevel, Long> numberOfLogsBySeverity(Date from, Date to) throws IndexerServiceException {
        SphinxClient sphinxClient = new SphinxClient();

        setMatchMode(sphinxClient);

        setDate(from, to, sphinxClient);

        try {
            sphinxClient.SetGroupBy("severity", SphinxClient.SPH_GROUPBY_ATTR);
        } catch (SphinxException e) {
            LOG.error("While trying to set group by severity", e);
            throw new IndexerServiceException("While trying to set group by severity", e);
        }

        SphinxResult result = querySphinx("", sphinxClient);

        Map<LogLevel, Long> resultMap = initTreeMapNumberOfLogsBySeverity();
        if (errorOccuredWhileQuerying(sphinxClient, result)) return resultMap;

        reportWarningsIfAny(sphinxClient);

        for(SphinxMatch sm: result.matches) {
            LogLevel level = null;
            Long count     = null;

            for (int i= 0; i < sm.attrValues.size(); i++) {
                if ("@groupby".equalsIgnoreCase(result.attrNames[i])) {
                    level = LogLevel.levelForValue(((Long)sm.attrValues.get(i)).intValue());
                } else if ("@count".equalsIgnoreCase(result.attrNames[i])) {
                    count = (Long) sm.attrValues.get(i);
                }
            }

            if (level != null && count !=null) {
                resultMap.put(level, count);
            } else {
                throw new IndexerServiceException("Cannot find group date or count value in the search result");
            }
        }
        return resultMap;
    }

    @Override
    public Map<Date, Long> numberOfLogsByDate(Date from, Date to, Integer offset, Integer limit) throws IndexerServiceException {

        SphinxClient sphinxClient = createSphinxClient();

        setMatchMode(sphinxClient);

        setDate(from, to, sphinxClient);

        setLimits(offset, limit, sphinxClient);

        try {
            sphinxClient.SetGroupBy("log_date", SphinxClient.SPH_GROUPBY_DAY);
        } catch (SphinxException e) {
            LOG.error("While trying to set group by", e);
            throw new IndexerServiceException("While trying to set group by", e);
        }

        SphinxResult result = querySphinx("", sphinxClient);

        if (errorOccuredWhileQuerying(sphinxClient, result)) return Collections.emptyMap();

        reportWarningsIfAny(sphinxClient);

        Map<Date, Long> resultMap = new TreeMap<Date, Long>();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        for(SphinxMatch sm: result.matches) {
            Date groupDate = null;
            Long count     = null;

            for (int i= 0; i < sm.attrValues.size(); i++) {
                if ("@groupby".equalsIgnoreCase(result.attrNames[i])) {
                    try {
                        groupDate = df.parse((String.valueOf(sm.attrValues.get(i))));
                    } catch (ParseException e) {
                        LOG.error("While parsing group date", e);
                        throw new IndexerServiceException("While parsing group date", e);
                    }
                } else if ("@count".equalsIgnoreCase(result.attrNames[i])) {
                    count = (Long) sm.attrValues.get(i);
                }
            }

            if (groupDate != null && count !=null) {
                resultMap.put(groupDate, count);
            } else {
                throw new IndexerServiceException("Cannot find group date or count value in the search result");
            }
        }

        return resultMap;
    }

    private  Map<LogLevel, Long> initTreeMapNumberOfLogsBySeverity(){
        Map<LogLevel, Long> resultMap = new TreeMap<LogLevel, Long>();
        for (LogLevel logLevel : LogLevel.values()) {
            resultMap.put(logLevel, 0L);
        }
        return resultMap;
    }

    private void setMatchMode(SphinxClient sphinxClient) throws IndexerServiceException {
        try {
            sphinxClient.SetMatchMode(SphinxClient.SPH_MATCH_EXTENDED2);
        } catch (SphinxException e) {
            LOG.error("While trying setting sphinx match mode", e);
            throw new IndexerServiceException("While trying setting sphinx match mode", e);
        }
    }

    private void setAttributeFilter(List<Integer> filterValues, SphinxClient sphinxClient, String attributeName) throws IndexerServiceException {
        if (filterValues != null && filterValues.size() > 0) {
            try {
                sphinxClient.SetFilter(attributeName, toIntArray(filterValues), false);
            } catch (SphinxException e) {
                throw new IndexerServiceException("While trying to set " + attributeName + " filter", e);
            }

        }
    }

    private long toUnixTime(Date from) {
        return from.getTime()/1000;
    }

    private int[] toIntArray(List<Integer> list) {
        int[] array = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
