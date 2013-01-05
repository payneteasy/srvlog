set names utf8 collate utf8_general_ci;
drop table if exists logs;

create table logs(
  log_id           int(10) unsigned not null auto_increment,
  log_date         datetime,
  facility         int(3),
  severity         int(3),
  host             varchar(60),
  message      text,
  primary key pk_log(log_id)
)
engine = innodb;

commit;