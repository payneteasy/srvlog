package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.impl.SimpleLogCollector;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * Date: 14.01.13 Time: 15:33
 */
public class SearchLogCollectorTest {

    @Test
    public void searchLogs() {
        SimpleLogCollector logCollector = new SimpleLogCollector();

//        INIT MOCK SERVICES
        ILogDao mockLogDao = createMock(ILogDao.class);
        logCollector.setLogDao(mockLogDao);
        IIndexerService mockIndexerService = createMock(IIndexerService.class);
        logCollector.setIndexerService(mockIndexerService);

        List<Integer> facilities = Arrays.asList(1, 2, 3, 4);
        List<Integer> severities = Arrays.asList(1, 2, 3, 4);
        List<Integer> host = Arrays.asList(1, 2, 3, 4);
        ;
        String pattern = "";
        int offset = 0;
        int limit = 25;

        expect(mockIndexerService.search(facilities, severities, host, pattern, offset, limit)).andReturn(getIds());

        expect(mockLogDao.getLogsByIds(StringUtils.collectionToCommaDelimitedString(getIds()))).andReturn(getLogs());

        replay(mockLogDao, mockIndexerService);

        logCollector.search(facilities, severities, host, pattern, offset, limit);

        verify(mockLogDao, mockIndexerService);

    }



    private List<Long> getIds() {
        return Arrays.asList(1L, 2L, 3L);
    }


    public List<LogData> getLogs() {
        return new ArrayList<LogData>();
    }
}
