drop database if exists srvlog;

create database `srvlog` default character set utf8 collate utf8_general_ci;

grant all privileges on srvlog.* to 'srvlog'@'localhost' identified by '123srvlog123' with grant option;

grant grant option on srvlog.* to 'srvlog'@'localhost';

set global log_bin_trust_function_creators=1;