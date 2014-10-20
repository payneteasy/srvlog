drop procedure if exists get_log_by_id;
delimiter $$
create procedure get_log_by_id(i_log_id int (10))
main_sql:
  begin
     select l.log_id,
            l.log_date,
            h.hostname host,
            l.severity,
            l.facility,
            l.message,
            l.program,
            l.hash,
            l.has_snort_logs
       from logs l
            join hosts h on l.host_id = h.host_id
      where l.log_id = i_log_id;
  end
$$
delimiter ;
call save_routine_information('get_log_by_id',
                              concat_ws(',',
                                        'log_id int',
                                        'log_date datetime',
                                        'host varchar',
                                        'severity int',
                                        'facility int',
                                        'message varchar',
                                        'program varchar',
                                        'hash varchar',
                                        'has_snort_logs int'
                                       )
                             );