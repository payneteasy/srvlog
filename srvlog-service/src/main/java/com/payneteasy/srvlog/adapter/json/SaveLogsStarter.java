package com.payneteasy.srvlog.adapter.json;

import com.payneteasy.http.server.HttpServer;
import com.payneteasy.startup.parameters.StartupParametersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Service
public class SaveLogsStarter {

    private static final Logger LOG = LoggerFactory.getLogger(SaveLogsStarter.class);

    private final ISaveLogsService saveLogsService;
    private final String           ipAddress;
    private final int              port;
    private final String           token;
    private final String           path;

    private HttpServer server;
    private Thread     thread;

    @Autowired
    public SaveLogsStarter(ISaveLogsService saveLogsService
    ) {
        this.saveLogsService = saveLogsService;

        IJsonAdapterConfig config = StartupParametersFactory.getStartupParameters(IJsonAdapterConfig.class);

        ipAddress = config.getWebServerBindAddress();
        port      = config.getWebServerPort();
        token     = config.getToken();
        path      = config.getPath();
    }

    @PostConstruct
    public void startup() {
        LOG.info("Starting json adapter ...");

        SaveLogsHttpRequestHandler handler = new SaveLogsHttpRequestHandler(saveLogsService, path, token);

        try {
            server = new HttpServer(
                    new InetSocketAddress(ipAddress, port)
                    , new HttpLoggerSlf()
                    , Executors.newCachedThreadPool()
                    , handler
                    , 10_000
            );
        } catch (IOException e) {
            throw new IllegalStateException("Cannot start web server for json transport", e);
        }

        thread = new Thread(() -> server.acceptSocketAndWait());
        thread.setName("json-"+ port);
        thread.start();

    }

    @PreDestroy
    public void destroy() {
        LOG.info("Shutting down json adapter ...");
        if(server != null) {
            server.stop();
        } else {
            LOG.warn("Server is null");
        }

        if(thread != null) {
            thread.interrupt();
        } else {
            LOG.warn("Thread is null");
        }
    }
}
