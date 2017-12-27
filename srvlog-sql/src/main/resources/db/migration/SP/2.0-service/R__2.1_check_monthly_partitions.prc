drop procedure if exists check_monthly_partitions;
delimiter $$
create procedure check_monthly_partitions(i_table_name              varchar(128),
                                          i_data_date               datetime,
                                          i_max_partitions_count    int(10)
                                         )
  begin
    declare v_av_partitions_count   int(10);
    declare v_part_year             char(4);
    declare v_part_month            char(2);
    declare v_partition_name        varchar(128);
    declare ex_no_records_found     int(10) default 0;

    declare
      cur_expired_patitiones cursor for select partition_name
                                          from information_schema.partitions
                                         where     table_schema = database()
                                               and table_name = i_table_name
                                               and substring(partition_name, length(i_table_name) + 2) <
                                                     cast(
                                                       date_format(
                                                         date_sub(i_data_date, interval i_max_partitions_count month),
                                                         '%Y%m'
                                                       ) as signed
                                                     );

    declare continue handler for not found begin select 1 into ex_no_records_found from (select 1) t; end;
    select case
             when date_format(i_data_date, '%m') = '12' then
               lpad(cast(date_format(i_data_date, '%Y') as signed) + 1, 4, '0')
             else
               date_format(i_data_date, '%Y')
           end,
           case
             when date_format(i_data_date, '%m') = '12' then '01'
             else lpad(cast(date_format(i_data_date, '%m') as signed) + 1, 2, '0')
           end
      into v_part_year, v_part_month
      from dual;

    select count(1)
      into v_av_partitions_count
      from information_schema.partitions
     where     table_name = i_table_name
           and substring(partition_name, length(i_table_name) + 2) >= cast(date_format(i_data_date, '%Y%m') as signed)
           and table_schema = database();

    if v_av_partitions_count = 0 then
      set @sv_ddl_statement      =
            concat('alter table ',
                   i_table_name,
                   ' add partition (partition ',
                   i_table_name,
                   '_',
                   date_format(i_data_date, '%Y%m'),
                   ' values less than(',
                   v_part_year,
                   v_part_month,
                   '))'
                  );

      prepare v_stmt from @sv_ddl_statement;
      execute v_stmt;

      deallocate prepare v_stmt;
    end if;

    set ex_no_records_found   = 0;

    open cur_expired_patitiones;

    repeat
      fetch cur_expired_patitiones into v_partition_name;

      if not ex_no_records_found then
        set @sv_ddl_statement      =
              concat('alter table ',
                     i_table_name,
                     ' drop partition ',
                     v_partition_name
                    );

        prepare v_stmt from @sv_ddl_statement;
        execute v_stmt;

        deallocate prepare v_stmt;
        set ex_no_records_found   = 0;
      end if;
    until ex_no_records_found
    end repeat;

    close cur_expired_patitiones;
  end
$$
delimiter ;