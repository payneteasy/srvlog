package com.payneteasy.srvlog;

import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.LogData;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Date: 04.01.13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-test-datasource.xml","classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class LogDaoTest {

    @BeforeClass
    public static void setUpDB() throws IOException, InterruptedException {
        DatabaseUtil.createDatabase(new String[]{"bash", "./create_database.sh"});
    }

    @Autowired
    ILogDao logDao;

    @Test
    public void testSaveLog(){
        LogData logData = new LogData();
        logData.setSeverity(1);
        logData.setDate(new Date());
        logData.setFacility(1);
        logData.setMessage("message");
        logData.setHost("localhost");

        logDao.saveLog(logData);
        assertNotNull("ID must be assigned to logData entity", logData.getId());

    }


}
