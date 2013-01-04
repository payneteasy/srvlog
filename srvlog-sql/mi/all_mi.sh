#!/bin/bash

. ../functions.sh

for i in `ls | grep -v UT | sort` ; do
    if [ -d "$i" ]; then
        logInfo "-  $i"
        ( cd "$i" && ls ./*.sh > /dev/null 2>&1 && bash ./*.sh )
    fi
done

exit 0
