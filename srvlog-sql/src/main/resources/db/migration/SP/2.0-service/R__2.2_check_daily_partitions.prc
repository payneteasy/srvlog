drop procedure if exists check_daily_partitions;
delimiter $$
create procedure check_daily_partitions(i_table_name              varchar(128),
                                        i_data_date               datetime,
                                        i_max_partitions_count    int(10)
                                       )
  begin
    declare v_av_partitions_count     int(10);
    declare v_future_partition        varchar(128);
    declare v_future_partition_desc   varchar(128);
    declare v_part_name               char(8);
    declare v_partition_name          varchar(128);
    declare ex_no_records_found       int(10) default 0;

    declare
      cur_expired_patitiones cursor for select partition_name
                                          from information_schema.partitions
                                         where     table_schema = database()
                                               and table_name = i_table_name
                                               and substring(partition_name, length(i_table_name) + 2) <
                                                     cast(
                                                       date_format(
                                                         date_sub(i_data_date, interval i_max_partitions_count day),
                                                         '%Y%m%d'
                                                       ) as signed
                                                     );

    declare continue handler for not found begin select 1 into ex_no_records_found from (select 1) t; end;
    select date_format(date(date_add(i_data_date, interval 1 day)), '%Y%m%d') into v_part_name from dual;

    select count(1)
      into v_av_partitions_count
      from information_schema.partitions
     where     table_name = i_table_name
           and substring(partition_name, length(i_table_name) + 2) = cast(date_format(i_data_date, '%Y%m%d') as signed)
           and table_schema = database();

    select min(partition_name), min(partition_description)
      into v_future_partition, v_future_partition_desc
      from information_schema.partitions
     where     table_name = i_table_name
           and substring(partition_name, length(i_table_name) + 2) > cast(date_format(i_data_date, '%Y%m%d') as signed)
           and table_schema = database();

    if v_av_partitions_count = 0 and v_future_partition is null then
      set @sv_ddl_statement      =
            concat('alter table ',
                   i_table_name,
                   ' add partition (partition ',
                   i_table_name,
                   '_',
                   date_format(i_data_date, '%Y%m%d'),
                   ' values less than(',
                   v_part_name,
                   '))'
                  );

      prepare v_stmt from @sv_ddl_statement;
      execute v_stmt;

      deallocate prepare v_stmt;
    elseif v_av_partitions_count = 0 and v_future_partition is not null then
      set @sv_ddl_statement      =
            concat('alter table ',
                   i_table_name,
                   ' reorganize partition ',
                   v_future_partition,
                   ' into (partition ',
                   i_table_name,
                   '_',
                   date_format(i_data_date, '%Y%m%d'),
                   ' values less than(',
                   v_part_name,
                   '), partition ',
                   v_future_partition,
                   ' values less than (',
                   v_future_partition_desc,
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