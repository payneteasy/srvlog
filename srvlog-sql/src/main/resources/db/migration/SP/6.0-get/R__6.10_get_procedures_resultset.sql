drop procedure if exists get_procedures_resultset;
delimiter $$
create procedure get_procedures_resultset()
  begin
   set session group_concat_max_len   = 10240;

    select routine_name specific_name,
           group_concat(concat(argument_name, ' ', argument_type)
                          order by ordinal_number
           )
             routine_resultset
      from mysql_routines_return_arguments
    group by routine_name;
  end
$$
delimiter ;
call save_routine_information('get_procedures_resultset',
                              concat('specific_name varchar', ', routine_resultset varchar')
                             );