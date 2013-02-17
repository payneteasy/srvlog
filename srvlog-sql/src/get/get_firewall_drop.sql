drop procedure if exists get_firewall_drop;
delimiter $$
create procedure get_firewall_drop(i_drop_date datetime)
 main_sql:
  begin
    select   case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' SRC=', -1), ' ', 1)
               else
                 'startup'
             end
               source_ip,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' DST=', -1), ' ', 1)
               else
                 'startup'
             end
               destination_ip,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' SPT=', -1), ' ', 1)
               else
                 'startup'
             end
               source_port,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' DPT=', -1), ' ', 1)
               else
                 'startup'
             end
               destination_port,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' PROTO=', -1), ' ', 1)
               else
                 'startup'
             end
               protocol,
             count(1) drop_count
        from logs l, hosts h
       where     log_date >= date(i_alert_date)
             and log_date < date_add(date(i_alert_date), interval 1 day)
             and h.host_id = l.host_id
             and h.hostname = 'firewall' and l.program is null
    group by case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' SRC=', -1), ' ', 1)
               else
                 'startup'
             end,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' DST=', -1), ' ', 1)
               else
                 'startup'
             end,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' SPT=', -1), ' ', 1)
               else
                 'startup'
             end,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' DPT=', -1), ' ', 1)
               else
                 'startup'
             end,
             case
               when l.message like '%Drop % packet:%' then
                 substring_index(substring_index(l.message, ' PROTO=', -1), ' ', 1)
               else
                 'startup'
             end
    order by drop_count desc;
  end
$$
delimiter ;
call save_routine_information('get_firewall_drop',
                              concat_ws(',',
                                        'source_ip varchar',
                                        'destination_ip varchar',
                                        'source_port varchar',
                                        'destination_port varchar',
                                        'protocol varchar',
                                        'drop_count int'
                                       )
                             );