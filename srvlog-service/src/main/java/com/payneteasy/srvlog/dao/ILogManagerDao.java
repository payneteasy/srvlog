package com.payneteasy.srvlog.dao;

import com.payneteasy.srvlog.data.LogData;

import java.util.List;

/**
 * Date: 03.01.13
 */
public interface ILogManagerDao {

    void saveLog(LogData log);

}
