package com.payneteasy.srvlog.service;

/**
 * Date: 14.01.13
 * Time: 20:13
 */
public class IndexerServiceException extends Exception {

    public IndexerServiceException(String message) {
        super(message);
    }

    public IndexerServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
