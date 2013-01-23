drop procedure if exists save_log;
delimiter $$
create procedure save_log(out o_log_id int (10),
                          i_log_date datetime,
                          i_facility int(3),
                          i_severity int(3),
                          i_host     varchar(60),
                          i_message  text,
                          i_program  varchar (60)

)

main_sql:
  begin
     declare v_host_id  int(10);

     select host_id
       into v_host_id
       from hosts
      where hostname = i_host;

      if v_host_id is not null then
           insert into logs(log_date, logs_partition_key, facility, severity, host_id, message, program)
           values (i_log_date, date_format(log_date, "%Y%m"), i_facility, i_severity, v_host_id, i_message, i_program);
      else
           insert into unprocessed_logs(log_date, facility, severity, host, message, program)
           values (i_log_date, i_facility, i_severity, i_host, i_message, i_program);
      end if;
     set o_log_id = last_insert_id();
  end
$$
delimiter ;