#!/bin/bash

. ../functions.sh

echo "Drop and create database $MYSQL_DATABASE"
runScriptRoot create_srvlog_db.sql
die "Cannot create database"

(
cd ../mi
for i in `ls | grep '^R' | sort` ; do
    if [ -d "$i" ]; then
        logInfo "-  $i"
        ( cd "$i" && ls ./*.sh > /dev/null 2>&1 && bash ./*.sh )
	return_code=$?
	if [ "$return_code" != '0' ]; then
		logError "Error $return_code"
		exit $return_code
	fi
    fi
done
)

( cd ../src && ./all-proc.sh )
return_code=$?
if [ "$return_code" != '0' ]; then
    logError "Error $return_code"
    exit $return_code
fi

exit 0
