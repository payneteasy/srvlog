package com.payneteasy.srvlog.wicket.component.navigation.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 16.01.13
 * Time: 13:53
 */
@Service
public class FakeDataLoaderService {

    public List<FakeData> loadFakePageableList(int offset, int limit) {
        List testDataList = getTestData();
        return testDataList.subList(offset, Math.min(testDataList.size(), offset + limit));
    }

    public List getTestData() {
        List<FakeData> result = new LinkedList<FakeData>();
        for (int i = 0; i < 15; i++) {
             result.add(new FakeData(new Long(i), "message" + i));
        }
        return result;
    }

    public List getEmptyData() {
        return Collections.emptyList();
    }

}
