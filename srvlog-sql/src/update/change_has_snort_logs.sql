drop procedure if exists change_has_snort_logs;

delimiter $$
create procedure change_has_snort_logs(i_log_id int(10), i_has_snort_logs int(1))
main_sql:
  begin
     update logs l
     set l.has_snort_logs = i_has_snort_logs
     where l.log_id = i_log_id;
  end
$$
delimiter ;