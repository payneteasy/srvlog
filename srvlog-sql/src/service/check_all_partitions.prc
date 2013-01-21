drop procedure if exists check_all_partitions;
delimiter $$
create procedure check_all_partitions(i_partitioning_type varchar(1))
 main_sql:
  begin
    declare v_partition_date         datetime default date_add(date(now()), interval 1 day);
    declare v_partition_hour         datetime default date_add(now(), interval 1 hour);
    declare ex_no_records_found      int(10) default 0;
    declare v_table_name             varchar(128);
    declare v_partitioning_type      varchar(1);
    declare v_max_partitions_count   int(10);

    declare
      cur_patitioned_tales cursor for
        select table_name, partitioning_type, max_partitions_count
          from paynet_partitioned_tables
         where partitioning_type = coalesce(i_partitioning_type, partitioning_type);

    declare continue handler for not found set ex_no_records_found = 1;
    set ex_no_records_found   = 0;

    open cur_patitioned_tales;

    repeat
      fetch cur_patitioned_tales
      into v_table_name, v_partitioning_type, v_max_partitions_count;

      if not ex_no_records_found then
        if v_partitioning_type = 'M' then
          call check_monthly_partitions(v_table_name, v_partition_date, v_max_partitions_count);
        elseif v_partitioning_type = 'D' then
          call check_daily_partitions(v_table_name, v_partition_date, v_max_partitions_count);
        elseif v_partitioning_type = 'H' then
          call check_hourly_partitions(v_table_name, v_partition_hour, v_max_partitions_count);
        end if;

        set ex_no_records_found   = 0;
      end if;
    until ex_no_records_found
    end repeat;

    close cur_patitioned_tales;
  end
$$
delimiter ;