package com.payneteasy.srvlog.data;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class SrvlogDbMigrator {

    private static final Logger logger = LoggerFactory.getLogger(SrvlogDbMigrator.class);

    private String url;
    private String user;
    private String password;
    private String driver;
    private String[] schemas;
    private String[] sqlMigrationSuffixes;
    private String baselineVersion;
    private String baselineDescription;

    public SrvlogDbMigrator(Properties props) {
        this.url = props.getProperty("flyway.url", "jdbc:mariadb://localhost/srvlog?characterEncoding=utf8&amp;useInformationSchema=true&amp;noAccessToProcedureBodies=true&amp;useLocalSessionState=true&amp;autoReconnect=false");
        this.user = props.getProperty("flyway.user", "srvlog");
        this.password = props.getProperty("flyway.password", "123srvlog123");
        this.schemas = props.getProperty("flyway.schemas", "srvlog").split(",");
        this.baselineVersion = props.getProperty("flyway.baselineVersion", "4.0");
        this.baselineDescription = props.getProperty("flyway.baselineDescription", "Migration from bash");
        this.driver = props.getProperty("flyway.driver", "org.mariadb.jdbc.Driver");
        this.sqlMigrationSuffixes = props.getProperty("flyway.sqlMigrationSuffixes", ".sql,.prc").split(",");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String[] getSchemas() {
        return schemas;
    }

    public void setSchemas(String[] schemas) {
        this.schemas = schemas;
    }

    public String[] getSqlMigrationSuffixes() {
        return sqlMigrationSuffixes;
    }

    public void setSqlMigrationSuffixes(String[] sqlMigrationSuffixes) {
        this.sqlMigrationSuffixes = sqlMigrationSuffixes;
    }

    public String getBaselineVersion() {
        return baselineVersion;
    }

    public void setBaselineVersion(String baselineVersion) {
        this.baselineVersion = baselineVersion;
    }

    public String getBaselineDescription() {
        return baselineDescription;
    }

    public void setBaselineDescription(String baselineDescription) {
        this.baselineDescription = baselineDescription;
    }

    public void cleanDatabase() {
        Flyway fw = buildFlywayInstance();
        fw.clean();
    }

    private Flyway buildFlywayInstance() {
        Flyway fw = Flyway.configure()
                .dataSource(url, user, password)
                .baselineVersion(MigrationVersion.fromVersion(baselineVersion))
                .baselineDescription(baselineDescription)
                .schemas(schemas)
                .sqlMigrationSuffixes(sqlMigrationSuffixes).load();
/*
        Flyway fw = new Flyway();
        fw.setDataSource(url, user, password);
        fw.setBaselineVersion(MigrationVersion.fromVersion(baselineVersion));
        fw.setBaselineDescription(baselineDescription);
        fw.setSchemas(schemas);
        fw.setSqlMigrationSuffixes(sqlMigrationSuffixes);
*/
        return fw;
    }

    public void migrateDatabase() {
        Flyway flyway = buildFlywayInstance();
        flyway.migrate();
    }

    public void baselineDatabase() {
        buildFlywayInstance().baseline();
    }

    public void repairDatabase() {
        buildFlywayInstance().repair();
    }

    public void validateDatabase() {
        buildFlywayInstance().validate();
    }

    public void infoDatabase() {
        buildFlywayInstance().info();
    }

    public static void main(String[] args) throws IOException {

        logger.info("Check for configFile first");
        String configFile = System.getProperty("configFile");
        SrvlogDbMigrator dbMigrator = getInstance(configFile);
        if(args.length > 0 ) {
            String command = args[0];
            if (Objects.equals("clean", command)) {
                dbMigrator.cleanDatabase();
            } else if (Objects.equals("migrate", command)) {
                dbMigrator.migrateDatabase();
            } else if (Objects.equals("baseline", command)) {
                dbMigrator.baselineDatabase();
            } else if (Objects.equals("repair", command)) {
                dbMigrator.repairDatabase();
            } else if (Objects.equals("validate", command)) {
                dbMigrator.validateDatabase();
            } else if (Objects.equals("info", command)) {
                dbMigrator.infoDatabase();
            } else {
                System.err.println("No command specified");
                System.exit(1);
            }

        } else {
            System.out.println("Usage: srvlog-sql.jar [Optional]-DconfigFile=/path/to/config.properties [Required]command\n" +
                    "\tCommands Description: \n" +
                    "\t\tclean - removes all entities from the database\n" +
                    "\t\tmigrate - performs migration up to the latest available version\n" +
                    "\t\tbaseline - baselines existing database to version 4.0\n" +
                    "\t\tinfo - prints information details about current database state\n" +
                    "\t\tvalidate - performs current database validation against the provided migrations\n" +
                    "\t\trepair - repairs the database by removing failed migrations from the metadata"

            );
        }

    }

    public static SrvlogDbMigrator getInstance(String configFile) throws IOException {
        Properties props = new Properties();
        if (configFile != null) {
            logger.info("configFile=" + configFile + " found. Load properties");
            try (Reader reader = Files.newBufferedReader(Paths.get(configFile))) {
                props.load(reader);
            }
        } else {
            logger.info("No configFile found.");
        }
        return new SrvlogDbMigrator(props);
    }

}
