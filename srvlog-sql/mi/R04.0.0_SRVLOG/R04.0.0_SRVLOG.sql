set names utf8 collate utf8_general_ci;

drop table if exists unprocessed_snort_logs;

insert         into snort_logs_new(id,
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
          select distinct s.id,
                 date_format(s.date, "%Y%m"),
                 s.program,
                 s.sensor_name,
                 s.date,
                 s.priority,
                 s.classification,
                 s.alert_cause,
                 s.generator_id,
                 s.signature_id,
                 s.signature_revision,
                 s.protocol_number,
                 s.protocol_alias,
                 s.protocol_version,
                 s.source_ip,
                 s.destination_ip,
                 s.header_length,
                 s.service_type,
                 s.datagram_length,
                 s.identification,
                 s.flags,
                 s.fragment_offset,
                 s.time_to_live,
                 s.checksum,
                 s.source_port,
                 s.destination_port,
                 s.host,
                 s.x_forwarded_for,
                 s.x_real_ip,
                 s.payload
            from snort_logs s, tmp_mi_snort_logs_identifiers t
   where t.id = s.id
on duplicate key update snort_logs_partition_key = date_format(s.date, "%Y%m"),
                        program = s.program,
                        sensor_name = s.sensor_name,
                        date = s.date,
                        priority = s.priority,
                        classification = s.classification,
                        alert_cause = s.alert_cause,
                        generator_id = s.generator_id,
                        signature_id = s.signature_id,
                        signature_revision = s.signature_revision,
                        protocol_number = s.protocol_number,
                        protocol_alias = s.protocol_alias,
                        protocol_version = s.protocol_version,
                        source_ip = s.source_ip,
                        destination_ip = s.destination_ip,
                        header_length = s.header_length,
                        service_type = s.service_type,
                        datagram_length = s.datagram_length,
                        identification = s.identification,
                        flags = s.flags,
                        fragment_offset = s.fragment_offset,
                        time_to_live = s.time_to_live,
                        checksum = s.checksum,
                        source_port = s.source_port,
                        destination_port = s.destination_port,
                        host = s.host,
                        x_forwarded_for = s.x_forwarded_for,
                        x_real_ip = s.x_real_ip,
                        payload = s.payload;

drop trigger if exists trg_snort_logs_ai;
drop trigger if exists trg_snort_logs_au;
drop table if exists tmp_mi_snort_logs_identifiers;
rename table snort_logs to snort_logs_500_old, snort_logs_new to snort_logs;

drop event if exists ev_check_snort_logs_partitions;

create event ev_check_snort_logs_partitions
    on schedule every 4 hour starts now()
    comment 'Check snort_logs table partitions'
    do call check_monthly_partitions('snort_logs', date_add(now(), interval 1 day), 6);

alter table unprocessed_logs
    drop index idx_unprocessed_logs_hash,
    drop column has_snort_logs
;

commit;