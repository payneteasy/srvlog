How to run integration tests?
===

1) Make sure `sphinxsearch` indexer and `searchd` are installed 
in the operation system and path to them is declared in the `PATH` 
environment variable;  
2) Run MariDB (MySQL) instance and create database using using 
srvlog-sql/db/create_srvlog_db.sql by running the following command

`mysql -u root -p < create_srvlog_db.sql`

3) Once MariaDB schema and user are successfully created run maven with 
integration-test profile enabled

`mvn clean test -Pintegration-test` 

That's it!