drop procedure if exists get_ossec_logs;
delimiter $$
create procedure get_ossec_logs(i_identifier varchar(60), i_date_from datetime, i_date_to datetime)
main_sql:
  begin
     select     l.id,
                l.log_id,
                l.date,
                l.identifier,
                l.hash

       from ossec_logs l
      where l.identifier = i_identifier
        and l.date between i_date_from and i_date_to
    order by l.date desc, l.id desc;
  end
$$
delimiter ;
call save_routine_information('get_ossec_logs',
                              concat_ws(',',
                                        'id int',
                                        'log_id int',
                                        'date datetime',
                                        'identifier varchar',
                                        'hash varchar'
                                       )
                             );
