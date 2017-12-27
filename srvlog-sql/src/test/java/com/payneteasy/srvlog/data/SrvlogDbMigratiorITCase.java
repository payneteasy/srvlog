package com.payneteasy.srvlog.data;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

import static org.junit.Assert.assertEquals;


public class SrvlogDbMigratiorITCase {

    static JdbcTemplate jdbc;
    static SrvlogDbMigrator srvlogDbMigrator;

    @BeforeClass
    public static void setUp() throws ClassNotFoundException, IOException {

        Properties properties = new Properties();
        properties.load(SrvlogDbMigratiorITCase.class.getClassLoader().getResourceAsStream("test-flyway.properties"));
        srvlogDbMigrator = new SrvlogDbMigrator(properties);
        jdbc = new JdbcTemplate(getDataSource(srvlogDbMigrator));
        //Connection connection = DriverManager.getConnection("");
    }

    private static DataSource getDataSource(SrvlogDbMigrator srvlogDbMigrator) throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<? extends Driver>) Class.forName(srvlogDbMigrator.getDriver()));
        dataSource.setUrl(srvlogDbMigrator.getUrl());
        dataSource.setUsername(srvlogDbMigrator.getUser());
        dataSource.setPassword(srvlogDbMigrator.getPassword());
        return dataSource;
    }

    @Test
    public void testCleanDatabase() {
        srvlogDbMigrator.cleanDatabase();

        final LinkedList<String> tableList = new LinkedList<>();
        jdbc.query("show tables", resultSet -> {
            tableList.add(resultSet.getString(1));
        });

        assertEquals("After clean database should not contain any tables",0, tableList.size());
    }

    @Test
    public void testMigrateDatabase() {
        srvlogDbMigrator.migrateDatabase();

        final LinkedList<String> tableList = new LinkedList<>();
        jdbc.query("show tables", resultSet -> {
            tableList.add(resultSet.getString(1));
        });

        assertEquals("9 tables should be returned.\n " +
                "+---------------------------------+\n" +
                "| Tables_in_srvlog                |\n" +
                "+---------------------------------+\n" +
                "| flyway_schema_history           |\n" +
                "| hosts                           |\n" +
                "| logs                            |\n" +
                "| mysql_routines_return_arguments |\n" +
                "| ossec_logs                      |\n" +
                "| snort_logs                      |\n" +
                "| snort_logs_500_old              |\n" +
                "| sph_counter                     |\n" +
                "| unprocessed_logs                |\n" +
                "+---------------------------------+"
        , 9, tableList.size());

    }

}
