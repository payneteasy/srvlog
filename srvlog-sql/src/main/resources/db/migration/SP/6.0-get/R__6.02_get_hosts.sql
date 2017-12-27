drop procedure if exists get_hosts;
delimiter $$
create procedure get_hosts()
main_sql:
  begin
     select h.host_id,
            h.hostname,
            h.ip
       from hosts h
      order by h.hostname;
  end
$$
delimiter ;
call save_routine_information('get_hosts',
                              concat_ws(',',
                                        'host_id int',
                                        'hostname varchar',
                                        'ip varchar'
                                       )
                             );