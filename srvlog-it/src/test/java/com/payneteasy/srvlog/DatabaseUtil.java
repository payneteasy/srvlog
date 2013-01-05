package com.payneteasy.srvlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

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
}
