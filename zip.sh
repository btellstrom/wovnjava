#!/bin/bash

version=`grep '^    <version>' pom.xml | sed -e 's/    <version>//' | sed -e 's!</version>!!'`

zip target/wovnjava-jar-${version}.zip -jr target/wovnjava-*.jar licenses/*

