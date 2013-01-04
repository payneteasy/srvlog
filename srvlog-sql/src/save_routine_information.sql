drop procedure if exists save_routine_information;
delimiter $$
create procedure save_routine_information(i_routine_name varchar(128), i_resultset_definition varchar(10000))
  begin
    declare v_argument_name          varchar(128);
    declare v_argument_type          varchar(128);
    declare v_resultset_definition   varchar(10000);
    declare v_ordinal_number         int(10);
    set v_resultset_definition   = i_resultset_definition;
    set v_ordinal_number         = 1;

    delete from mysql_routines_return_arguments
          where routine_name = i_routine_name;

    while v_resultset_definition <> concat(coalesce(v_argument_name, ''), ' ', coalesce(v_argument_type, ''))
    do
      select trim(both ' ' from substring_index(v_resultset_definition, ' ', 1)),
             trim(both ' ' from substring(substring_index(v_resultset_definition, ',', 1),
                                          length(substring_index(v_resultset_definition, ' ', 1)) + 1
                                         )),
             trim(both ' ' from substring(
                                  v_resultset_definition,
                                    case
                                      when instr(v_resultset_definition, ',') = 0 then length(v_resultset_definition)
                                      else instr(v_resultset_definition, ',')
                                    end
                                  + 1
                                ))
        into v_argument_name, v_argument_type, v_resultset_definition
        from dual;

      if coalesce(v_argument_name, '') <> '' then
        insert into mysql_routines_return_arguments(routine_name,
                                                    argument_name,
                                                    argument_type,
                                                    ordinal_number
                                                   )
             values (i_routine_name,
                     v_argument_name,
                     v_argument_type,
                     v_ordinal_number
                    );
      end if;

      set v_ordinal_number   = v_ordinal_number + 1;
    end while;
  end
$$
delimiter ;