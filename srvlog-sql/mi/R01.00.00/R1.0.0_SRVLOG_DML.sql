set names utf8 collate utf8_general_ci;

drop event if exists ev_check_log_partitions;

create event ev_check_log_partitions
    on schedule every 1 day starts now()
    comment 'Check log table partitions'
    do call check_monthly_partitions('logs', date(now()));

commit;