package com.payneteasy.srvlog.service;

import java.util.List;

/**
 * Date: 14.01.13 Time: 15:38
 */
public interface IIndexerService {

    List<Long> search(List<Integer> facilities, List<Integer> severities, List<Integer> hosts, String pattern, int startNum, int rowCount);
}
