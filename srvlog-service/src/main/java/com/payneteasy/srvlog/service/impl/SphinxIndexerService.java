package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.IndexerServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Date: 14.01.13 Time: 16:48
 */
@Transactional
@Service
public class SphinxIndexerService implements IIndexerService{

    private static final Logger LOG = LoggerFactory.getLogger(SphinxIndexerService.class);

    @Override
    public List<Long> search(Date from, Date to, List<Integer> facilities, List<Integer> severities, List<Integer> hosts, String pattern, Integer offset, Integer limit) throws IndexerServiceException {
        SphinxClient sphinxClient = new SphinxClient();

        if (from != null && to != null) {
            try {
                sphinxClient.SetFilterRange("log_date", toUnixTime(from), toUnixTime(to), false);
            } catch (SphinxException e) {
                LOG.error("While trying to set date range", e);
                throw new IndexerServiceException("While trying to set date range", e);
            }
        }


        setAttributeFilter(facilities, sphinxClient, "facility");

        setAttributeFilter(hosts, sphinxClient, "host_id");

        setAttributeFilter(severities, sphinxClient, "severity");

        if (offset != null && limit != null) {
            try {
                sphinxClient.SetLimits(offset, limit);
            } catch (SphinxException e) {
                LOG.error("While trying to set limits", e);
                throw new IndexerServiceException("While trying to set limits", e);
            }
        }
        if (pattern == null) {
            pattern = "";
        }

        SphinxResult result;
        try {
            result = sphinxClient.Query(pattern);
        } catch (SphinxException e) {
            LOG.error("While sending query to Sphinx Daemon", e);
            throw new IndexerServiceException("While sending query to Sphinx Daemon", e);
        }
        if (result == null || result.matches.length == 0) {
            if (StringUtils.isNotEmpty(sphinxClient.GetLastError())) {
                LOG.error("While sending query to Sphinx Daemon: " + sphinxClient.GetLastError());
                throw new IndexerServiceException("While sending query to Sphinx Daemon: " + sphinxClient.GetLastError());
            }
            return Collections.emptyList();
        }
        if (StringUtils.isNotEmpty(sphinxClient.GetLastWarning())) {
            LOG.warn("From Sphinx Daemon: " + sphinxClient.GetLastWarning());
        }

        List<Long> logIds = new LinkedList<Long>();
        for (SphinxMatch sm: result.matches) {
            logIds.add(sm.docId);
        }
       return logIds;
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
}
