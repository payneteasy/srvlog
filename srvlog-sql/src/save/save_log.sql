drop procedure if exists save_log;
delimiter $$
create procedure save_log(out o_log_id int (10),
                          i_log_date datetime,
                          i_facility int(3),
                          i_severity int(3),
                          i_host     varchar(60),
                          i_message  text

)
main_sql:
  begin
     insert into logs(log_date, facility, severity, host, message)
     values (i_log_date, i_facility, i_severity, i_host, i_message);

     set o_log_id = last_insert_id();
  end
$$
delimiter ;