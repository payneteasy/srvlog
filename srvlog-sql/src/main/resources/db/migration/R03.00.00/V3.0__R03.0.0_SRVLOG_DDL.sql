set names utf8 collate utf8_general_ci;
drop table if exists ossec_logs;

create table ossec_logs(
    id              int(10) unsigned not null auto_increment,
    log_id          int(10) unsigned not null,
    date            datetime not null,
    identifier      varchar(60) not null,
    hash            varchar(32),

    index pk_id(id),
    index idx_ossec_logs_log_id(log_id),
    index idx_ossec_logs_date(date),
    index idx_ossec_logs_identifier(identifier),
    index idx_ossec_logs_hash(hash)
)
engine = innodb;

commit;