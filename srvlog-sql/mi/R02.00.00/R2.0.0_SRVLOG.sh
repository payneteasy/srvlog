#!/bin/bash

. ../../functions.sh

runScript R2.0.0_SRVLOG.sql

runScript R2.0.0_SRVLOG_DML.sql

exit $?
