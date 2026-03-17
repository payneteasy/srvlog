package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.service.condition.MockModeCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Mock IIndexerService for visual UI testing without Sphinx.
 * Returns sample data so Bootstrap layout can be verified.
 */
@Service
@Conditional(MockModeCondition.class)
@Primary
public class MockIndexerService implements IIndexerService {

    private static final Logger LOG = LoggerFactory.getLogger(MockIndexerService.class);

    @Override
    public List<Long> search(Date from, Date to, List<Integer> facilities, List<Integer> severities,
                             List<Integer> hosts, String pattern, Integer offset, Integer limit)
            throws IndexerServiceException {
        return Collections.emptyList();
    }

    @Override
    public Map<Date, Long> numberOfLogsByDate(Date from, Date to) throws IndexerServiceException {
        Map<Date, Long> result = new TreeMap<>();
        result.put(from != null ? from : new Date(), 10L);
        return result;
    }

    @Override
    public Map<LogLevel, Long> numberOfLogsBySeverity(Date from, Date to) throws IndexerServiceException {
        LOG.info("MockIndexerService.numberOfLogsBySeverity called");
        Map<LogLevel, Long> result = new TreeMap<>();
        for (LogLevel level : LogLevel.values()) {
            result.put(level, (long) (level.ordinal() + 1));
        }
        return result;
    }

    @Override
    public Map<Date, Long> numberOfLogsByDate(Date fromDate, Date toDate, Integer offset, Integer limit)
            throws IndexerServiceException {
        return numberOfLogsByDate(fromDate, toDate);
    }
}
