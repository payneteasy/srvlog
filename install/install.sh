#!/bin/bash
mvn install:install-file -DgroupId=org.sphx.api -DartifactId=sphinx-api -Dpackaging=jar -Dversion=2.0.6 -Dfile=sphinxapi.jar  -DgeneratePom=true

mvn install:install-file -DgroupId=com.nesscomputing -DartifactId=ness-syslog4j -Dpackaging=jar -Dversion=0.9.47-NESS-8-SNAPSHOT -Dfile=ness-syslog4j-0.9.47-NESS-8-SNAPSHOT.jar -DpomFile=ness-syslog4j-0.9.47-NESS-8-SNAPSHOT.pom
