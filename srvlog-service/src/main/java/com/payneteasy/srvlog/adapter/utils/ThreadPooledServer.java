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

    protected int serverPort = 8080;
    private final IRunnableFacotry workerFacotory;
    protected ServerSocket serverSocket = null;
    protected boolean stopped = false;
    protected boolean started = false;
    protected Thread runningThread = null;
    protected ExecutorService threadPool =
            Executors.newFixedThreadPool(10);

    public ThreadPooledServer(int port, IRunnableFacotry workerFacotory) {
        this.serverPort = port;
        this.workerFacotory = workerFacotory;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
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
                    workerFacotory.createWorker(clientSocket));
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


