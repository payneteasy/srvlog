#!/bin/bash

. ../../functions.sh

runScript R1.0.0_SRVLOG.sql

runScript R1.0.0_SRVLOG_DML.sql

exit $?
