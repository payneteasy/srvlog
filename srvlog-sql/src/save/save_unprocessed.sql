drop procedure if exists save_unprocessed;
delimiter $$
create procedure save_unprocessed()
main_sql:
  begin
    create temporary table if not exists temp_logs_to_process(
      log_id           int(10) unsigned,
      log_date         datetime,
      facility         int(3),
      severity         int(3),
      host_id          int(10) unsigned,
      message          text,
      program          varchar(60),
      primary key temp_logs_to_process(log_id)
      )
      engine = myisam;

    truncate table temp_logs_to_process;

	insert into temp_logs_to_process(log_id, log_date, facility, severity, host_id, message, program)
	      select ul.log_id, ul.log_date, ul.facility, ul.severity, h.host_id, ul.message, ul.program
		    from unprocessed_logs ul, hosts h
		   where ul.host = h.hostname;

    insert into logs(log_date, logs_partition_key, facility, severity, host_id, message, program)
          select log_date, date_format(log_date, "%Y%m"), facility, severity, host_id, message, program
		    from temp_logs_to_process;
    
	delete ul from unprocessed_logs ul, temp_logs_to_process tl where ul.log_id = tl.log_id;
  end
$$
delimiter ;