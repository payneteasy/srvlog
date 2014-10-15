drop procedure if exists get_unprocessed_snort_logs_by_identifier;
delimiter $$
create procedure get_unprocessed_snort_logs_by_identifier(i_identifier varchar(60))
main_sql:
  begin
     select     l.id,
                l.date,
                l.identifier,
                l.message

       from unprocessed_snort_logs l
      where l.identifier = i_identifier;
  end
$$
delimiter ;
call save_routine_information('get_unprocessed_snort_logs_by_identifier',
                              concat_ws(',',
                                        'id int',
                                        'date datetime',
                                        'identifier varchar',
                                        'message varchar'
                                       )
                             );