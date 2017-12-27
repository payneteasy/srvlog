package com.payneteasy.srvlog.data;


import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SrvlogDbMigratorTest {


    @Test
    public void testReadPropertiesConfiguration() throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("new-test-flyway.properties"));

        SrvlogDbMigrator srvlogDbMigrator = new SrvlogDbMigrator(props);
        assertEquals("flyway.url is incorrect", "jdbc:mariadb://localhost/srvlog_new?characterEncoding=utf8&amp;useInformationSchema=true&amp;noAccessToProcedureBodies=true&amp;useLocalSessionState=true&amp;autoReconnect=false", srvlogDbMigrator.getUrl());
        assertEquals("flyway.user is incorrect", "srvlog_new", srvlogDbMigrator.getUser());
        assertEquals("flyway.password is incorrect","123srvlog123_new", srvlogDbMigrator.getPassword());
        assertEquals("flyway.driver is incorrect", "org.mariadb.jdbc.Driver", srvlogDbMigrator.getDriver());
        assertArrayEquals("flyway.schemas is incorrect", new String[]{"srvlog_new"}, srvlogDbMigrator.getSchemas());
        assertArrayEquals("flyway.sqlMigrationSuffixes is incorrect. Should be set by default", new String[]{".sql", ".prc"}, srvlogDbMigrator.getSqlMigrationSuffixes());
        assertEquals("flyway.baselineVersion is incorrect. Shouold be set by default", "4.0", srvlogDbMigrator.getBaselineVersion());
        assertEquals("flyway.baselineDescription is incorrect. Should be set by default.", "Migration from bash", srvlogDbMigrator.getBaselineDescription());
    }

}
