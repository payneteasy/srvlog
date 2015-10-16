drop procedure if exists get_ossec_logs_by_hash;
delimiter $$
create procedure get_ossec_logs_by_hash(i_hash varchar(32))
main_sql:
  begin
     select     l.id,
                l.log_id,
                l.date,
                l.identifier,
                l.hash

        from ossec_logs l
        where l.hash = i_hash;
  end
$$
delimiter ;
call save_routine_information('get_ossec_logs_by_hash',
                              concat_ws(',',
                                        'id int',
                                        'log_id int',
                                        'date datetime',
                                        'identifier varchar',
                                        'hash varchar'
                                       )
                             );
