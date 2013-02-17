drop procedure if exists get_ossec_alerts;
delimiter $$
create procedure get_ossec_alerts(i_alert_date datetime)
 main_sql:
  begin
    select   substring_index(substring_index(message, ' Rule: ', -1), ';', 1) ossec_alert_type,
             count(1) ossec_alert_count
        from logs
       where     log_date >= date(i_alert_date)
             and log_date < date_add(date(i_alert_date), interval 1 day)
             and program is null
             and message like '%ossec:%'
             and message like '% Rule: %'
    group by substring_index(substring_index(message, ' Rule: ', -1), ';', 1)
    order by 2 desc;
  end
$$
delimiter ;
call save_routine_information('get_ossec_alerts', concat_ws(',', 'ossec_alert_type varchar', 'ossec_alert_count varchar'));