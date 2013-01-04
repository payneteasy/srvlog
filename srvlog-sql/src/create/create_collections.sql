drop procedure if exists create_collections;
delimiter $$
create procedure create_collections()
  begin
    declare continue handler for sqlstate '42S01' begin end;

    set session group_concat_max_len   = 16384;

    select 1;
  end
$$
delimiter ;