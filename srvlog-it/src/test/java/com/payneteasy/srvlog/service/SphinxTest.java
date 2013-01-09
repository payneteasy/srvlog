package com.payneteasy.srvlog.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Date: 09.01.13
 * Time: 1:36
 */
public class SphinxTest {

    @Ignore
    @Test
    public void testQuerySearchd() throws SphinxException {


        SphinxClient client = new SphinxClient();
        client.SetMatchMode(SphinxClient.SPH_MATCH_EXTENDED);
        client.SetSelect("log_date, facility, severity, host");
        client.SetSortMode(SphinxClient.SPH_SORT_ATTR_DESC, "log_date");
        SphinxResult result = client.Query("message", "srvlog_index");

        System.out.println(result.matches.length + " results found.");
        for (SphinxMatch sm : result.matches) {
            System.out.println(sm.docId);
            System.out.println(sm.attrValues);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Long) sm.attrValues.get(0)) * 1000);


            System.out.println(calendar.getTime());
        }

        //SphinxClient client = new SphinxClient();
    }
}
