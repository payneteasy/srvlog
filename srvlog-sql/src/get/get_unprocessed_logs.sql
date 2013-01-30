drop procedure if exists get_unprocessed_logs;
delimiter $$
create procedure get_unprocessed_logs(i_num_of_logs int (10))
main_sql:
  begin
     select l.log_id,
                 l.log_date,
                 l.host,
                 l.severity,
                 l.facility,
                 l.message,
                 l.program
       from unprocessed_logs l
      order by l.log_date desc
      limit i_num_of_logs;
  end
$$
delimiter ;
call save_routine_information('get_unprocessed_logs',
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