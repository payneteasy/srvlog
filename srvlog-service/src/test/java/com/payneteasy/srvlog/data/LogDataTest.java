package com.payneteasy.srvlog.data;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Date: 04.01.13
 */

public class LogDataTest {

    @Test
    public void testEquality () {
        LogData logData1 = new LogData();
        logData1.setId(1L);
        Date date = Calendar.getInstance().getTime();
        logData1.setDate(date);
        logData1.setFacility(1);
        logData1.setHost("localhost");
        logData1.setSeverity(1);
        logData1.setMessage("message1");
        logData1.setProgram("program1");

        LogData logData2 = new LogData();
        logData2.setId(1L);
        logData2.setDate(date);
        logData2.setFacility(1);
        logData2.setHost("localhost");
        logData2.setSeverity(1);
        logData2.setMessage("message1");
        logData2.setProgram("program1");

        Assert.assertEquals(logData1, logData2);

    }

    @Test
    public void testNotEquality(){
        LogData logData1 = new LogData();
        logData1.setId(1L);
        Date date = Calendar.getInstance().getTime();
        logData1.setDate(date);
        logData1.setFacility(1);
        logData1.setHost("localhost");
        logData1.setSeverity(1);
        logData1.setMessage("message1");
        logData1.setProgram("program1");

        LogData logData2 = new LogData();
        logData2.setId(2L);
        logData2.setDate(date);
        logData2.setFacility(1);
        logData2.setHost("localhost");
        logData2.setSeverity(1);
        logData1.setMessage("message2");
        logData1.setProgram("program2");

        Assert.assertNotSame(logData1, logData2);
    }

}
