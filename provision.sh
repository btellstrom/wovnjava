#!/bin/bash

apt-get -y update

apt-get -y install\
 apache2\
 tomcat7 tomcat7-admin tomcat7-common tomcat7-docs tomcat7-examples\
 openjdk-6-jdk maven

a2enmod headers proxy proxy_html rewrite xml2enc
service apache2 restart

