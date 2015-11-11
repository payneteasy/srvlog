set names utf8 collate utf8_general_ci;

drop table if exists snort_logs_new;

create table snort_logs_new(
    id                           int(10) unsigned not null auto_increment,
    snort_logs_partition_key     int(6)  unsigned not null,
    -- snort methadata
    program                      varchar(60) not null,
    sensor_name                  varchar(60) not null,
    date                         datetime not null,
    priority                     int(2) unsigned not null,
    classification               varchar(255) not null,
    alert_cause                  varchar(255) not null,
    -- snort signature data
    generator_id                 int(10) unsigned not null,
    signature_id                 int(10) unsigned not null,
    signature_revision           int(10) unsigned not null,
    -- IP header data
    protocol_number              int(5) unsigned,
    protocol_alias               varchar(60),
    protocol_version             int(4) unsigned,
    source_ip                    varchar(60),
    destination_ip               varchar(60),
    header_length                int(4) unsigned,
    service_type                 int(6) unsigned,
    datagram_length              int(16) unsigned,
    identification               int(16) unsigned,
    flags                        int(3) unsigned,
    fragment_offset              int(13) unsigned,
    time_to_live                 int(8) unsigned,
    checksum                     int(16) unsigned,
    -- protocol header data
    source_port                  int(16) unsigned,
    destination_port             int(16) unsigned,
    -- http header data
    host                         varchar(60),
    x_forwarded_for              varchar(60),
    x_real_ip                    varchar(60),
    -- packet payload
    payload                      text not null,

    index pk_id(id),
    index idx_snort_logs_date(date),
    index idx_snort_logs_generator_id(generator_id),
    index idx_snort_logs_signature_id(signature_id)
)
engine = innodb
# auto_increment = 40000000
partition by range (snort_logs_partition_key)(
  partition snort_logs_201501 values less than (201502) engine = innodb,
  partition snort_logs_201502 values less than (201503) engine = innodb,
  partition snort_logs_201503 values less than (201504) engine = innodb,
  partition snort_logs_201504 values less than (201505) engine = innodb,
  partition snort_logs_201505 values less than (201506) engine = innodb,
  partition snort_logs_201506 values less than (201507) engine = innodb,
  partition snort_logs_201507 values less than (201508) engine = innodb,
  partition snort_logs_201508 values less than (201509) engine = innodb,
  partition snort_logs_201509 values less than (201510) engine = innodb,
  partition snort_logs_201510 values less than (201511) engine = innodb,
  partition snort_logs_201511 values less than (201512) engine = innodb
  );

drop table if exists tmp_mi_snort_logs_identifiers;

create table tmp_mi_snort_logs_identifiers (id int(10) unsigned)
engine = innodb;

drop trigger if exists trg_snort_logs_ai;
delimiter $$
create trigger trg_snort_logs_ai
  after insert
  on snort_logs
  for each row
begin
  insert into tmp_mi_snort_logs_identifiers(id)
       values (new.id);
end
$$

delimiter ;
drop trigger if exists trg_snort_logs_au;
delimiter $$
create trigger trg_snort_logs_au
  after update
  on snort_logs
  for each row
begin
  insert into tmp_mi_snort_logs_identifiers(id)
       values (new.id);
end
$$

delimiter ;
drop procedure if exists mi_copy_table_data;
delimiter $$
create procedure mi_copy_table_data(i_current_id      int(10),
                                    i_max_id          int(10),
                                    i_copy_count      int(10))
 main_sql:
  begin
    declare v_current_id         int(10) default i_current_id;

    copy_next: loop
      if v_current_id < i_max_id then
        insert into snort_logs_new(id,
                                   snort_logs_partition_key,
                                   program,
                                   sensor_name,
                                   date,
                                   priority,
                                   classification,
                                   alert_cause,
                                   generator_id,
                                   signature_id,
                                   signature_revision,
                                   protocol_number,
                                   protocol_alias,
                                   protocol_version,
                                   source_ip,
                                   destination_ip,
                                   header_length,
                                   service_type,
                                   datagram_length,
                                   identification,
                                   flags,
                                   fragment_offset,
                                   time_to_live,
                                   checksum,
                                   source_port,
                                   destination_port,
                                   host,
                                   x_forwarded_for,
                                   x_real_ip,
                                   payload)
          select id,
                 date_format(date, "%Y%m"),
                 program,
                 sensor_name,
                 date,
                 priority,
                 classification,
                 alert_cause,
                 generator_id,
                 signature_id,
                 signature_revision,
                 protocol_number,
                 protocol_alias,
                 protocol_version,
                 source_ip,
                 destination_ip,
                 header_length,
                 service_type,
                 datagram_length,
                 identification,
                 flags,
                 fragment_offset,
                 time_to_live,
                 checksum,
                 source_port,
                 destination_port,
                 host,
                 x_forwarded_for,
                 x_real_ip,
                 payload
            from snort_logs
           where id >= v_current_id
                 and id < v_current_id + i_copy_count;
        commit;

        set v_current_id = v_current_id + i_copy_count;
        iterate copy_next;
      else
        leave copy_next;
      end if;
    end loop;
    commit;
  end
$$
delimiter ;
call mi_copy_table_data(1, 40000000, 1000000);
drop procedure if exists mi_copy_table_data;