package com.payneteasy.srvlog.log4j;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.DatabaseUtil;
import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Date: 19.06.13
 * Time: 22:34
 */
public class Log4jAdapterAndSimpleLogCollectorIntegrationTest extends CommonIntegrationTest {

    private static final Logger log = Logger.getLogger(Log4jAdapterAndSimpleLogCollectorIntegrationTest.class);

    private ILogCollector logCollector;

    @Override
    protected void createSpringContext() {
        context =  new ClassPathXmlApplicationContext(
                "classpath:spring/spring-test-datasource.xml",
                "classpath:spring/spring-dao.xml",
                "classpath:spring/spring-service.xml",
                "classpath:spring/spring-log-adapter.xml"
        );
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        logCollector = context.getBean(ILogCollector.class);
        DatabaseUtil.addLocalhostToHostList(context.getBean(ILogDao.class));
    }

    @Test
    public void testRetrieveAndSaveLog4jMessage() throws InterruptedException, IOException {
        String message = "This is test logging";
        sendTestLog4Message(message);

        int numberOfLogs = 25;
        int numOfTries = 3;
        List<LogData> logDataList = null;
        for (int i = 0; i < numOfTries ; i++) {
            TimeUnit.SECONDS.sleep(2);
            logDataList = logCollector.loadLatest(numberOfLogs, null);
            if (logDataList.size() > 0) {
                break;
            }
        }

        assertTrue("Logs should be created in defined time interval", logDataList.size() > 0);
    }
    
    public static void sendTestLog4Message(String message) throws IOException {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 4712);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new LoggingEvent(Log.class.getName(), log, Level.ERROR, message, new RuntimeException("This is runtime exception")));
            oos.flush();
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(socket);
        }
    }

    public static void main(String[] args) throws IOException {
        sendTestLog4Message("Test Exception");
    }
    
}