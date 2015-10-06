set names utf8 collate utf8_general_ci;
drop table if exists mysql_routines_return_arguments;

create table mysql_routines_return_arguments (
  routine_name varchar(128) not null,
  argument_name varchar(128) not null,
  argument_type varchar(128) not null,
  ordinal_number int(10),
  unique key unq_mysql_routines_arguments_name_type (routine_name, argument_name)
)
engine = innodb;

\. run_install_command.sql
\. run_install_cond_command.sql
\. save_routine_information.sql

\. service/check_all_partitions.prc
\. service/check_monthly_partitions.prc
\. service/check_daily_partitions.prc

\. save/save_host.sql
\. save/save_log.sql
\. save/save_unprocessed.sql
\. save/save_snort_log.sql
\. save/save_unprocessed_snort_log.sql
\. save/save_ossec_log.sql

\. create/create_collections.sql

\. int/raise_application_error.sql

\. get/get_procedures_resultset.sql
\. get/get_log_by_id.sql
\. get/get_latest.sql
\. get/get_hosts.sql
\. get/get_unprocessed_log_by_id.sql
\. get/get_logs_by_ids.sql
\. get/get_firewall_drop.sql
\. get/get_firewall_alerts.sql
\. get/get_ossec_alerts.sql
\. get/get_unprocessed_logs.sql
\. get/get_unprocessed_hosts_name.sql
\. get/get_logs_by_hash.sql
\. get/get_snort_logs_by_hash.sql
\. get/get_unprocessed_snort_logs.sql
\. get/get_ossec_logs.sql

\. delete/delete_all_unprocessed_snort_logs.sql

\. update/change_has_snort_logs.sql

commit;