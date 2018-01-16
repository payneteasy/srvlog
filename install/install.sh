#!/bin/bash

cd "$(dirname "$0")"

mvn install:install-file -DgroupId=org.sphx.api -DartifactId=sphinx-api -Dpackaging=jar -Dversion=2.2.11 -Dfile=sphinxapi.jar  -DgeneratePom=true

mvn install:install-file -DgroupId=com.nesscomputing -DartifactId=ness-syslog4j -Dpackaging=jar -Dversion=0.9.47-NESS-8-SNAPSHOT -Dfile=ness-syslog4j-0.9.47-NESS-8-SNAPSHOT.jar -DpomFile=ness-syslog4j-0.9.47-NESS-8-SNAPSHOT.pom

mvn install:install-file -DgroupId=mysql -DartifactId=mysql-connector-java -Dpackaging=jar -Dversion=5.1.22-3 -Dfile=mysql-connector-java-5.1.22-3-bin.jar  -DgeneratePom=true
