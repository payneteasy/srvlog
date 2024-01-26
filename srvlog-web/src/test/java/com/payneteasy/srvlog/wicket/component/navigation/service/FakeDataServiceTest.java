package com.payneteasy.srvlog.wicket.component.navigation.service;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 16.01.13
 * Time: 13:59
 */
public class FakeDataServiceTest {

    @Test
    public void testPagingInTestData() {
        //should return totally 15 items
        FakeDataLoaderService service = new FakeDataLoaderService();
        List<FakeData> dataList = service.loadFakePageableList(0, 10);
        assertEquals(10, dataList.size());

        dataList = service.loadFakePageableList(10, 10);

        assertEquals(5, dataList.size());
    }
}
