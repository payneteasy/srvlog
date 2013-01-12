drop procedure if exists get_latest;
delimiter $$
create procedure get_latest(i_num_of_logs int (10))
main_sql:
  begin
     select l.log_id,
                 l.log_date,
                 h.hostname host,
                 l.severity,
                 l.facility,
                 l.message
       from logs l
       join hosts h on l.host_id = h.host_id
      order by l.log_date desc
      limit i_num_of_logs;
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
                                        'message varchar'
                                       )
                             );