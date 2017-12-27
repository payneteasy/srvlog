drop procedure if exists save_host;
delimiter $$
create procedure save_host(out o_host_id int (10),
                          i_hostname varchar(120),
                          i_ip varchar(60)
)
main_sql:
  begin
     insert into hosts(hostname, ip)
     values (i_hostname, i_ip);

     set o_host_id = last_insert_id();
  end
$$
delimiter ;