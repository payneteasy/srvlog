package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;

import java.util.List;

public class LogBroadcastingResponse {

    private boolean success;
    private List<LogData> logDataList;
    private String errorMessage;

    public LogBroadcastingResponse() {
    }

    public LogBroadcastingResponse(boolean success, List<LogData> logDataList, String errorMessage) {
        this.success = success;
        this.logDataList = logDataList;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<LogData> getLogDataList() {
        return logDataList;
    }

    public void setLogDataList(List<LogData> logDataList) {
        this.logDataList = logDataList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}