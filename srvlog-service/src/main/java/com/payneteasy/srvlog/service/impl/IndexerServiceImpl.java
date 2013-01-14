package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.service.IIndexerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 14.01.13 Time: 16:48
 */
@Transactional
@Service
public class IndexerServiceImpl implements IIndexerService{
    @Override
    public List<Long> search(List<Integer> facilities, List<Integer> severities, List<Integer> hosts, String pattern, int startNum, int rowCount) {
        return null;
    }
}
