drop procedure if exists save_ossec_log;
delimiter $$

create procedure save_ossec_log(out o_id          int (10),
                                i_log_id          int (10),
                                i_date            datetime,
                                i_identifier      varchar(60),
                                i_hash            varchar(32))

main_sql:
  begin
    insert into ossec_logs(log_id, date, identifier, hash)
    values (i_log_id, i_date, i_identifier, i_hash);

    set o_id = last_insert_id();
  end
$$
delimiter ;