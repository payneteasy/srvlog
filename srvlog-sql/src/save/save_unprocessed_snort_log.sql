drop procedure if exists save_unprocessed_snort_log;
delimiter $$
create procedure save_unprocessed_snort_log(out o_id int (10),
                          i_date            datetime,
                          i_identifier      varchar(60),
                          i_message         text)

main_sql:
  begin
    insert into unprocessed_snort_logs(date, identifier, message, unprocessed_snort_logs_partition_key)
    values (i_date, i_identifier, i_message, date_format(now(), "%Y%m%d"));

    set o_id = last_insert_id();
  end
$$
delimiter ;