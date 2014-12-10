set names utf8 collate utf8_general_ci;
drop table if exists unprocessed_snort_logs;

create table unprocessed_snort_logs(
    id              int(10) unsigned not null auto_increment,
    date            datetime not null,
    identifier      varchar(60) not null,
    message         text not null,
    unprocessed_snort_logs_partition_key  int(6)  unsigned not null,
    index pk_id(id),
    index idx_snort_unprocessed_logs_date(date),
    index idx_snort_unprocessed_logs_identifier(identifier)
)
engine = innodb
partition by range (unprocessed_snort_logs_partition_key)(
  partition unprocessed_snort_logs_100000 values less than(100001)
  );

drop table if exists snort_logs;

create table snort_logs(
    id                  int(10) unsigned not null auto_increment,
    hash                varchar(32) not null,
    -- snort methadata
    program             varchar(60) not null,
    sensor_name         varchar(60) not null,
    date                datetime not null,
    priority            int(2) unsigned not null,
    classification      varchar(255) not null,
    alert_cause         varchar(255) not null,
    -- snort signature data
    generator_id        int(10) unsigned not null,
    signature_id        int(10) unsigned not null,
    signature_revision  int(10) unsigned not null,
    -- IP header data
    protocol_number     int(5) unsigned,
    protocol_alias      varchar(60),
    protocol_version    int(4) unsigned,
    source_ip           varchar(60),
    destination_ip      varchar(60),
    header_length       int(4) unsigned,
    service_type        int(6) unsigned,
    datagram_length     int(16) unsigned,
    identification      int(16) unsigned,
    flags               int(3) unsigned,
    fragment_offset     int(13) unsigned,
    time_to_live        int(8) unsigned,
    checksum            int(16) unsigned,
    -- protocol header data
    source_port         int(16) unsigned,
    destination_port    int(16) unsigned,
    -- http header data
    host                varchar(60),
    x_forwarded_for     varchar(60),
    x_real_ip           varchar(60),
    -- packet payload
    payload             text not null,

    primary key pk_id(id),
    index idx_snort_logs_hash(hash)
)
engine = innodb;

alter table logs
    add column hash varchar(32),
    add column has_snort_logs int(1) not null default 0,
    add index idx_logs_hash(hash)
;

alter table unprocessed_logs
    add column hash varchar(32),
    add column has_snort_logs int(1) not null default 0,
    add index idx_unprocessed_logs_hash(hash)
;

commit;