drop procedure if exists get_logs_by_hash;
delimiter $$
create procedure get_logs_by_hash(i_hash varchar(32))
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
      where l.hash = i_hash;
  end
$$
delimiter ;
call save_routine_information('get_logs_by_hash',
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