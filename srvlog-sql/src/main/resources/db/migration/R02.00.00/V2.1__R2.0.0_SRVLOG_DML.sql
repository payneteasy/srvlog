set names utf8 collate utf8_general_ci;

drop event if exists ev_check_unprocessed_snort_logs_partitions;

create event ev_check_unprocessed_snort_logs_partitions
    on schedule every 1 day starts now()
    comment 'Check unprocessed_snort_logs table partitions'
    do call check_daily_partitions('unprocessed_snort_logs', date_add(now(), interval 1 day), 2);

commit;