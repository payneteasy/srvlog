set names utf8 collate utf8_general_ci;
drop table if exists logs;

create table logs(
  log_id           int(10) unsigned not null auto_increment,
  log_date         datetime,
  facility         int(10),
  severity         int(10),
  host             varchar(60),
  log_message      text,
  primary key pk_log(log_id)
)
engine = innodb;

commit;