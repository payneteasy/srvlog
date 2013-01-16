package com.payneteasy.srvlog;

import com.nesscomputing.syslog4j.SyslogIF;
import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Date: 04.01.13
 */
public class DatabaseUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtil.class);

    public static void createDatabase(String[] createDbCommand) throws IOException, InterruptedException {
        if("true".equals(System.getProperty("skip.createdb"))) {
            LOG.warn("Skipped creating database");
        } else {
            LOG.info("Creating database structure ...  (use -Dskip.createdb=true to skip)");
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(createDbCommand);
            if(LOG.isInfoEnabled()) {
                new Thread(new ProcessStreamReader(false, process.getInputStream())).start();
            }
            new Thread(new ProcessStreamReader(true, process.getErrorStream())).start();

            int result = process.waitFor();
            if(result!=0) {
                throw new IllegalStateException("Error creating database [error_code="+result+"]");
            }
        }
    }


    public static Process runCommand(List<String> parameters, File workingDir) throws IOException {
        LOG.info("Running {}", parameters);
        ProcessBuilder pb = new ProcessBuilder(parameters);

        if (workingDir != null) {
            pb.directory(workingDir);
        }

        Process process = pb.start();
        if(LOG.isInfoEnabled()) {
            new Thread(new ProcessStreamReader(false, process.getInputStream())).start();
        }
        new Thread(new ProcessStreamReader(true, process.getErrorStream())).start();

        return process;
    }

    public static void runCommandAndWaitUntilFinished(List<String> parameters, File workingDir) throws IOException, InterruptedException {
        Process process = runCommand(parameters, workingDir);
        int result = process.waitFor();
        if(result!=0) {
            throw new IllegalStateException("Error executing "+ parameters + "  [error_code="+result+"]");
        }
    }


    private static class ProcessStreamReader implements Runnable {

        public ProcessStreamReader(boolean aIdErrorLevel, InputStream aIn) {
            theIsErrorLevel = aIdErrorLevel;
            theIn = aIn;
        }

        public void run() {
            LineNumberReader in = new LineNumberReader(new InputStreamReader(theIn));
            String line;
            try {
                while ( (line=in.readLine())!=null) {
                    if(theIsErrorLevel) {
                        LOG.error(line);
                    } else {
                        LOG.info(line);
                    }
                }
            } catch (IOException e) {
                LOG.error("IO error", e);
            } finally {
                try {
                    theIn.close();
                } catch(Exception e) {
                    LOG.error("Cannot close input stream", e);
                }
            }
        }

        private final boolean theIsErrorLevel;
        private final InputStream theIn;
    }

    public static void generateTestLogsThroughSyslogClient(SyslogIF syslogClient) {
        Calendar c = Calendar.getInstance();
        c.set(2012, 0, 1, 0, 0, 0);

        List<Integer> facilityList = Arrays.asList(0, 1, 2, 3); // from 0 to 23
//        kern(0), user(1), mail(2), daemon(3), auth(4), syslog(5),
//                lpr(6), news(7), uucp(8), cron(9), authpriv(10), ftp(11),
//                ntp(12), audit(13), alert(14), clock(15),
//                local0(16), local1(17), local2(18), local3(19), local4(20),
//                local5(21), local6(22), local7(23);


        List<Integer> severityList = Arrays.asList(0, 1, 2, 3); // from 0 to 6
        // EMERGENCY(0), ALERT(1), CRITICAL(2), ERROR(3), WARN(4),  NOTICE(5), INFO(6), DEBUG(7);

        List<String> hosts = Arrays.asList("host1", "host2");

        int numOfDates = 10;
        List<Date> dates = new ArrayList<Date>(numOfDates);

        for (int i = 0; i < numOfDates; i++) {
            c.roll(Calendar.DAY_OF_YEAR, 1);
            dates.add(c.getTime());
        }
        //System.out.println(dates);

        for(Date d: dates) {
            c.setTime(d);
            for (int i = 0; i < facilityList.size(); i++) {

                c.roll(Calendar.HOUR_OF_DAY, 1);

                for (int j = 0; j < hosts.size(); j++) {

                    syslogClient.warn("A warning from syslog client with additional info for testing long logs messages. This message should be properly wrapped on the front-end.");
                    syslogClient.info("A info from syslog client");
                }
            }

        }
    }





    public static void generateTestLogs(ILogCollector logCollector) {


        Calendar c = Calendar.getInstance();
        c.set(2012, 0, 1, 0, 0, 0);

        List<Integer> facilityList = Arrays.asList(LogFacility.kern.getValue(),
                                                   LogFacility.user.getValue(),
                                                   LogFacility.mail.getValue(),
                                                   LogFacility.daemon.getValue());

        List<Integer> severityList = Arrays.asList(LogLevel.EMERGENCY.getValue(),
                                                    LogLevel.ALERT.getValue(),
                                                    LogLevel.CRITICAL.getValue(),
                                                    LogLevel.ERROR.getValue());

        List<String> hosts = Arrays.asList("host1", "host2");


        addTestHostsToDatabase(logCollector);

        int numOfDates = 10;
        List<Date> dates = new ArrayList<Date>(numOfDates);

        for (int i = 0; i < numOfDates; i++) {
            c.roll(Calendar.DAY_OF_YEAR, 1);
            dates.add(c.getTime());
        }
        System.out.println(dates);

        for(Date d: dates) {
            c.setTime(d);
            for (int i = 0; i < facilityList.size(); i++) {

                c.roll(Calendar.HOUR_OF_DAY, 1);

                for (int j = 0; j < hosts.size(); j++) {

                    LogData logData = new LogData();
                    logData.setDate(c.getTime());
                    logData.setFacility(i);
                    logData.setSeverity(i);
                    logData.setHost(hosts.get(j));
                    logData.setMessage("Big Log message from host " + hosts.get(j) + " for testing long messages on the UI");
                    logCollector.saveLog(logData);
                    System.out.println(logData);
                }

            }

        }
    }

    private static void addTestHostsToDatabase(ILogCollector logCollector) {

        List<HostData> hostDataList = logCollector.loadHosts();


        HostData host1 = new HostData();
        host1.setHostname("host1");
        if (!containsHost(hostDataList, host1)) {
            logCollector.saveHost(host1);
        }

        HostData host2 = new HostData();
        host2.setHostname("host2");
        if (!containsHost(hostDataList, host1)) {
            logCollector.saveHost(host2);
        }

    }

    private static boolean containsHost(List<HostData> hostDataList, HostData host) {
        for (HostData hostData : hostDataList) {
            if(host.getHostname().equals(hostData.getHostname())) {
                 return true;
            }
        }
        return false;

    }


    public static void addLocalhostToHostList(ILogDao logDao) {
        HostData hostData = new HostData();
        hostData.setHostname("localhost");
        hostData.setIpAddress("127.0.0.1");
        logDao.saveHost(hostData);


    }

    public static void main(String[] args) {
        DatabaseUtil.generateTestLogs(null);
    }
}
