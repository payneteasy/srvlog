set names utf8 collate utf8_general_ci;
drop table if exists logs;

create table logs(
  log_id              int(10) unsigned not null auto_increment,
  log_date            datetime,
  logs_partition_key  int(6)  unsigned not null,
  facility            int(3),
  severity            int(3),
  host_id             int(10),
  message             text,
  program             varchar(60),
  index pk_log(log_id),
  index idx_logs_host_log_date(host_id, log_date),
  index idx_logs_log_date(log_date)
)
engine = innodb
partition by range (logs_partition_key)(
  partition logs_100000 values less than(100001)
  );

drop table if exists unprocessed_logs;

create table unprocessed_logs(
  log_id           int(10) unsigned not null auto_increment,
  log_date         datetime,
  facility         int(3),
  severity         int(3),
  host             varchar(60),
  message          text,
  program          varchar(60),
  primary key pk_log(log_id)
)
engine = innodb;


drop table if exists hosts;

create table hosts (
   host_id           int(10) unsigned not null auto_increment,
   hostname          varchar(120),
   ip                varchar(60),
   primary key pk_host(host_id),
   unique unq_hosts_hostname(hostname)
) engine = innodb;


drop table if exists sph_counter;

create table sph_counter (
   index_id           int(3)  unsigned not null,
   max_doc_id         int(10)  unsigned not null,
   primary key pk_sph_counter(index_id)
) engine = innodb;



commit;