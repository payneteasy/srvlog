drop procedure if exists get_snort_logs;
delimiter $$
create procedure get_snort_logs(
                            i_generator_id        int(10),
                            i_signature_id        int(10),
                            i_signature_revision  int(10),
                            i_date_from datetime,
                            i_date_to datetime
                        )
main_sql:
  begin
    select
        l.id,
        -- snort methadata
        l.program,
        l.sensor_name,
        l.date,
        l.priority,
        l.classification,
        l.alert_cause,
        -- snort signature data
        l.generator_id,
        l.signature_id,
        l.signature_revision,
        -- IP header data
        l.protocol_number,
        l.protocol_alias,
        l.protocol_version,
        l.source_ip,
        l.destination_ip,
        l.header_length,
        l.service_type,
        l.datagram_length,
        l.identification,
        l.flags,
        l.fragment_offset,
        l.time_to_live,
        l.checksum,
        -- protocol header data
        l.source_port,
        l.destination_port,
        -- http header data
        l.host,
        l.x_forwarded_for,
        l.x_real_ip,
        -- packet payload
        l.payload

        from snort_logs l
        where l.generator_id = i_generator_id
        and l.signature_id = i_signature_id
        and l.signature_revision = i_signature_revision
        and l.date between i_date_from and i_date_to
        order by l.date desc, l.id desc
        limit 0, 10;
  end
$$
delimiter ;
call save_routine_information('get_snort_logs',
                              concat_ws(',',
                                        'id int',
                                        -- snort methadata
                                        'program varchar',
                                        'sensor_name varchar',
                                        'date datetime',
                                        'priority int',
                                        'classification varchar',
                                        'alert_cause varchar',
                                        -- snort signature data
                                        'generator_id int',
                                        'signature_id int',
                                        'signature_revision int',
                                        -- IP header data
                                        'protocol_number int',
                                        'protocol_alias varchar',
                                        'protocol_version int',
                                        'source_ip varchar',
                                        'destination_ip varchar',
                                        'header_length int',
                                        'service_type int',
                                        'datagram_length int',
                                        'identification int',
                                        'flags int',
                                        'fragment_offset int',
                                        'time_to_live int',
                                        'checksum int',
                                        -- protocol header data
                                        'source_port int',
                                        'destination_port int',
                                        -- http header data
                                        'host varchar',
                                        'x_forwarded_for varchar',
                                        'x_real_ip varchar',
                                        -- packet payload
                                        'payload varchar'
                                       )
                             );