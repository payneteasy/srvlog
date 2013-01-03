package com.payneteasy.dao;

import java.util.List;

/**
 * Date: 03.01.13
 */
public interface ILogManagerDao {
    void saveLogMessage(String message);
    List<String> getLogs();
}
