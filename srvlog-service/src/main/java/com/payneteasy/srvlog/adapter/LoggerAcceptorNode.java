package com.payneteasy.srvlog.adapter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;

//Copied partially from log4j library
public abstract class LoggerAcceptorNode<T> implements Runnable {

    private ObjectInputStream ois;
    private final Socket socket;
    private final Logger logger;

    public LoggerAcceptorNode(Socket socket, Logger logger) {
        this.socket = socket;
        this.logger = logger;
        try {
            ois = new ObjectInputStream(
                    new BufferedInputStream(socket.getInputStream()));
        } catch(InterruptedIOException e) {
            Thread.currentThread().interrupt();
            logger.error("Could not open ObjectInputStream to "+socket, e);
        } catch(IOException e) {
            logger.error("Could not open ObjectInputStream to "+socket, e);
        } catch(RuntimeException e) {
            logger.error("Could not open ObjectInputStream to "+socket, e);
        }

    }

    @Override
    public void run() {
        try {
            if (ois != null) {
                while(true) {
                    @SuppressWarnings("unchecked") T event = (T) ois.readObject();
                    processEvent(socket, event);
                }
            }
        } catch(java.io.EOFException e) {
            logger.info("Caught java.io.EOFException closing conneciton.");
        } catch(java.net.SocketException e) {
            logger.info("Caught java.net.SocketException closing conneciton.");
        } catch(InterruptedIOException e) {
            Thread.currentThread().interrupt();
            logger.info("Caught java.io.InterruptedIOException: "+e);
            logger.info("Closing connection.");
        } catch(IOException e) {
            logger.info("Caught java.io.IOException: "+e);
            logger.info("Closing connection.");
        } catch(Exception e) {
            logger.error("Unexpected exception. Closing conneciton.", e);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(socket);
        }
    }

    protected abstract void processEvent(Socket socket, T event);
}
