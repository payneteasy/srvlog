drop procedure if exists get_firewall_alerts;
delimiter $$
create procedure get_firewall_alerts(i_alert_date datetime)
 main_sql:
  begin
    select   case
               when l.program = 'snort' and l.message like '% [Classification: %' then
                 substring_index(substring_index(message, " [", 2), "] ", -1)
               when l.program = 'snort' then
                 'startup'
               else
                 l.program
             end
               alert_class,
             count(1) alert_count
        from logs l, hosts h
       where     log_date >= date(i_alert_date)
             and log_date < date_add(date(i_alert_date), interval 1 day)
             and h.host_id = l.host_id
             and h.hostname = 'firewall'
             and l.program is not null
    group by case
               when l.program = 'snort' and l.message like '% [Classification: %' then
                 substring_index(substring_index(message, " [", 2), "] ", -1)
               when l.program = 'snort' then
                 'startup'
               else
                 l.program
             end
    order by alert_count desc;
  end
$$
delimiter ;
call save_routine_information('get_firewall_alerts', concat_ws(',', 'alert_class varchar', 'alert_count varchar'));