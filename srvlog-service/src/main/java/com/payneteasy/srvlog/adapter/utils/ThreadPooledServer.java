package com.payneteasy.srvlog.adapter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 19.06.13
 * Time: 23:20
 */
public class ThreadPooledServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPooledServer.class);

    private final int serverPort;
    private final IRunnableFactory workerFactory;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private ServerSocket serverSocket = null;
    private boolean stopped = false;
    private boolean started = false;

    public ThreadPooledServer(int port, IRunnableFactory workerFactory) {
        this.serverPort = port;
        this.workerFactory = workerFactory;
    }

    @Override
    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket;
            try {
                started = true;
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    logger.info("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            this.threadPool.execute(
                    workerFactory.createWorker(clientSocket));
        }
        this.threadPool.shutdown();
        logger.info("Server Stopped.");
    }


    public synchronized boolean isStopped() {
        return this.stopped;
    }

    public synchronized boolean isStarted() {
        return this.started;
    }

    public synchronized void stop() {
        this.stopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port: " + this.serverPort, e);
        }
    }
}
