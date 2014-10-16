drop procedure if exists delete_all_unprocessed_snort_logs;
delimiter $$
create procedure delete_all_unprocessed_snort_logs()
main_sql:
  begin
     truncate unprocessed_snort_logs;
  end
$$
delimiter ;