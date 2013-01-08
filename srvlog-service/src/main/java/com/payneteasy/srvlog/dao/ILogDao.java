package com.payneteasy.srvlog.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.payneteasy.srvlog.data.LogData;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 03.01.13
 */
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public interface ILogDao {

    @AStoredProcedure(name="save_log")
    void saveLog(LogData log);

    @AStoredProcedure(name="get_log_by_id")
    LogData load(Long id);

    @AStoredProcedure(name="get_latest")
    List<LogData> loadLatest(int numberOfLogs);
}
