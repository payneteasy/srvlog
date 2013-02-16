drop procedure if exists get_unprocessed_hosts_name;
delimiter $$
create procedure get_unprocessed_hosts_name()
main_sql:
  begin
     select distinct host from unprocessed_logs;
  end
$$
delimiter ;
call save_routine_information('get_unprocessed_hosts_name',
                              concat_ws(',',
                                        'host varchar'
                                       )
                             );