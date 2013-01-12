drop procedure if exists get_unprocessed_log_by_id;
delimiter $$
create procedure get_unprocessed_log_by_id(i_log_id int (10))
main_sql:
  begin
     select l.log_id,
            l.log_date,
            l.host,
            l.severity,
            l.facility,
            l.message
       from unprocessed_logs l
      where l.log_id = i_log_id;
  end
$$
delimiter ;
call save_routine_information('get_unprocessed_log_by_id',
                              concat_ws(',',
                                        'log_id int',
                                        'log_date datetime',
                                        'host varchar',
                                        'severity int',
                                        'facility int',
                                        'message varchar'
                                       )
                             );