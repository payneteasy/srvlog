drop procedure if exists get_snort_logs_by_log_id;
delimiter $$
create procedure get_snort_logs_by_log_id(i_log_id int(10))
main_sql:
  begin
    select     l.id, l.log_id, l.program, l.sensor_name, l.date, l.priority, l.classification, l.alert_cause, l.protocol_number,
               l.protocol_alias, l.protocol_version, l.source_ip, l.destination_ip, l.header_length, l.service_type,
               l.datagram_length, l.identification, l.flags, l.fragment_offset, l.time_to_live, l.checksum,
               l.source_port, l.destination_port, l.payload

       from snort_logs l
      where l.log_id = i_log_id;
  end
$$
delimiter ;
call save_routine_information('get_snort_logs_by_log_id',
                              concat_ws(',',
                                        'id int',
                                        'log_id int',
                                        'program varchar',
                                        'sensor_name varchar',
                                        'date datetime',
                                        'priority int',
                                        'classification varchar',
                                        'alert_cause varchar',
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
                                        'source_port int',
                                        'destination_port int',
                                        'payload varchar'
                                       )
                             );