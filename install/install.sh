#!/bin/bash
mvn install:install-file -DgroupId=org.sphx.api -DartifactId=sphinx-api -Dpackaging=jar -Dversion=2.0.6 -Dfile=sphinxapi.jar  -DgeneratePom=true