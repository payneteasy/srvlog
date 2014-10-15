drop procedure if exists save_snort_log;
delimiter $$

create procedure save_snort_log(out o_id int (10),
                            i_log_id          int(10),
                            -- snort methadata
                            i_program             varchar(60),
                            i_sensor_name         varchar(60),
                            i_date                datetime,
                            i_priority            int(2),
                            i_classification      varchar(60),
                            i_alert_cause         varchar(256),
                            -- IP header data
                            i_protocol_number     int(5),
                            i_protocol_alias      varchar(60),
                            i_protocol_version    int(4),
                            i_source_ip           varchar(60),
                            i_destination_ip      varchar(60),
                            i_header_length       int(4),
                            i_service_type        int(6),
                            i_datagram_length     int(16),
                            i_identification      int(16),
                            i_flags               int(3),
                            i_fragment_offset     int(13),
                            i_time_to_live        int(8),
                            i_checksum            int(16),
                            -- protocol header data
                            i_source_port         int(16),
                            i_destination_port    int(16),
                            -- packet payload
                            i_payload             text)

main_sql:
  begin
    insert into snort_logs(log_id, program, sensor_name, date, priority, classification,  alert_cause, protocol_number,
                           protocol_alias, protocol_version, source_ip, destination_ip, header_length, service_type,
                           datagram_length, identification, flags, fragment_offset, time_to_live, checksum,
                           source_port, destination_port, payload)
    values (i_log_id, i_program, i_sensor_name, i_date, i_priority, i_classification, i_alert_cause, i_protocol_number,
            i_protocol_alias, i_protocol_version, i_source_ip, i_destination_ip, i_header_length, i_service_type,
            i_datagram_length, i_identification, i_flags, i_fragment_offset, i_time_to_live, i_checksum, i_source_port,
            i_destination_port, i_payload);

    set o_id = last_insert_id();
  end
$$
delimiter ;