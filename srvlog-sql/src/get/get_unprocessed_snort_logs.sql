drop procedure if exists get_unprocessed_snort_logs;
delimiter $$
create procedure get_unprocessed_snort_logs(i_identifier varchar(60), i_date_from datetime, i_date_to datetime)
main_sql:
  begin
     select     l.id,
                l.date,
                l.identifier,
                l.message

       from unprocessed_snort_logs l
      where l.identifier = i_identifier
        and l.date between i_date_from and i_date_to
    order by l.date desc, l.id desc
      limit 0, 10;
  end
$$
delimiter ;
call save_routine_information('get_unprocessed_snort_logs',
                              concat_ws(',',
                                        'id int',
                                        'date datetime',
                                        'identifier varchar',
                                        'message varchar'
                                       )
                             );