drop procedure if exists get_latest;
delimiter $$
create procedure get_latest(i_num_of_logs int(10), i_host_id int(10))
 main_sql:
  begin
    if i_host_id is null then
      select   l.log_id,
               l.log_date,
               h.hostname host,
               l.severity,
               l.facility,
               l.message,
               l.program
          from logs l force index ( idx_logs_host_log_date ) join hosts h on l.host_id = h.host_id
         where l.host_id = i_host_id
      order by l.log_date desc
         limit 1;
    else
      select   l.log_id,
               l.log_date,
               h.hostname host,
               l.severity,
               l.facility,
               l.message,
               l.program
          from logs l force index ( idx_logs_log_date ) join hosts h on l.host_id = h.host_id
      order by l.log_date desc
         limit 1;
    end if;
  end
$$
delimiter ;
call save_routine_information('get_latest',
                              concat_ws(',',
                                        'log_id int',
                                        'log_date datetime',
                                        'host varchar',
                                        'severity int',
                                        'facility int',
                                        'message varchar',
                                        'program varchar'
                                       )
                             );