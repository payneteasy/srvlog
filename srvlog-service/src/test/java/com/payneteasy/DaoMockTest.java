package com.payneteasy;

import com.payneteasy.dao.ILogManagerDao;
import com.payneteasy.service.ILogManager;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Date: 04.01.13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-dao.xml"})
public class DaoMockTest {
    @Autowired
    private ILogManagerDao logManagerDao;

    @Test
    public void testLogData(){
       logManagerDao.saveLogMessage("test data");
        Assert.assertEquals("test data", logManagerDao.getLogs().get(0));
    }
}
