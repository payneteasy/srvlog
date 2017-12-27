drop procedure if exists raise_application_error;
delimiter $$
create procedure raise_application_error(i_java_error_i18n_code varchar(32))
 main_sql:
  begin    
    signal sqlstate '45000' set message_text = i_java_error_i18n_code;
  end
$$
delimiter ;