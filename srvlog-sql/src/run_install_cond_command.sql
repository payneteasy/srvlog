drop procedure if exists run_install_cond_command;
delimiter $$
create procedure run_install_cond_command(i_ddl_statement varchar(4000), i_ignore_error_code char(5), i_check_condition text)
  begin
    declare v_throw_exception   tinyint;
    declare v_ddl_statement     varchar(4000);
    declare v_error_code        char(5);
    declare continue handler for sqlstate '42S02' set v_error_code       = '42S02';
    -- Create index 42000
    declare continue handler for sqlstate '42000' set v_error_code       = '42000';
    -- Add column 42S21
    declare continue handler for sqlstate '42S21' set v_error_code       = '42S21';
    -- Change column 42S22
    declare continue handler for sqlstate '42S22' set v_error_code       = '42S22';
    -- Rename table
    declare continue handler for sqlstate '42S01' set v_error_code       = '42S01';
    
    if i_check_condition is not null then
      set @sv_check_statement   = i_check_condition;
      prepare v_stmt from @sv_check_statement;
      execute v_stmt;
      deallocate prepare v_stmt;
    end if;

    if @sv_run_ddl = 1 then
      set @sv_ddl_statement   = i_ddl_statement;
      prepare v_stmt from @sv_ddl_statement;
      execute v_stmt;

      if v_error_code <> i_ignore_error_code and i_ignore_error_code is not null then
        signal sqlstate '49000' set message_text = 'Wrong exception is raised!';
      end if;
    end if;
  end
$$
delimiter ;