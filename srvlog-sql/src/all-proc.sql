set names utf8 collate utf8_general_ci;
drop table if exists mysql_routines_return_arguments;

create table mysql_routines_return_arguments (
  routine_name varchar(128) not null,
  argument_name varchar(128) not null,
  argument_type varchar(128) not null,
  ordinal_number int(10),
  unique key unq_mysql_routines_arguments_name_type (routine_name, argument_name)
) 
engine = innodb;

\. run_install_command.sql
\. run_install_cond_command.sql

\. save_routine_information.sql

\. create/create_collections.sql

\. int/raise_application_error.sql

\. get/get_procedures_resultset.sql

\. save/save_log.sql

commit;