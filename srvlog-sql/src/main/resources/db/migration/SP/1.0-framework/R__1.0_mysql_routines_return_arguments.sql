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
