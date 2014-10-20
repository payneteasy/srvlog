package com.payneteasy.srvlog.dao;

import com.googlecode.jdbcproc.daofactory.annotation.AStoredProcedure;
import com.payneteasy.srvlog.data.*;
import com.payneteasy.srvlog.data.UnprocessedSnortLogData;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    List<LogData> loadLatest(int numberOfLogs, Long hostId);

    @AStoredProcedure(name="save_host")
    void saveHost(HostData hostData);

    @AStoredProcedure(name="get_hosts")
    List<HostData> loadHosts();

    @AStoredProcedure(name="get_unprocessed_log_by_id")
    LogData finInUnporocessed(Long id);

    @AStoredProcedure(name="get_logs_by_ids")
    List<LogData> getLogsByIds(String logIds);

    @AStoredProcedure(name="get_unprocessed_logs")
    List<LogData> loadUnprocessed(int count);

    @AStoredProcedure(name="save_unprocessed")
    void saveUnprocessedLogs();

    @AStoredProcedure(name="get_unprocessed_hosts_name")
    List<String> getUnprocessedHostsName();

    @AStoredProcedure(name="get_firewall_alerts")
    List<FirewallAlertData> getFirewallAlertData(Date date);

    @AStoredProcedure(name="get_firewall_drop")
    List<FireWallDropData> getFirewallDropData(Date date);

    @AStoredProcedure(name="get_ossec_alerts")
    List<OssecAlertData> getOssecAlertData(Date data);

    @AStoredProcedure(name = "save_unprocessed_snort_log")
    void saveUnprocessedSnortLog(UnprocessedSnortLogData rawSnortMessage);

    @AStoredProcedure(name = "save_snort_log")
    void saveSnortLog(SnortLogData snortLogData);

    @AStoredProcedure(name = "get_unprocessed_snort_logs")
    List<UnprocessedSnortLogData> getUnprocessedSnortLogs(String identifier, Date fromDate, Date toDate);

    @AStoredProcedure(name = "delete_all_unprocessed_snort_logs")
    void deleteAllUnprocessedSnortLogs();

    @AStoredProcedure(name = "get_logs_by_hash")
    List<LogData> getLogsByHash(String hash);

    @AStoredProcedure(name = "get_snort_logs_by_hash")
    List<SnortLogData> getSnortLogsByHash(String hash);
}
