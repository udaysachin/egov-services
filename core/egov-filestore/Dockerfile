FROM egovio/openjdk:8-jre-headless

MAINTAINER Sumanth<sumanth@egovernments.org>

#set environment variables for application.properties
ENV DB_NAME=mydb \
    SHOW_SQL=true \
    DB_HOST=localhost \
    DB_PORT=5432 \
    DB_USER=postgres \
    DB_PASSWORD=postgres \
    GENERATE_DDL=true

# INSTRUCTIONS ON HOW TO BUILD JAR:
# Move to the location where pom.xml is exist in project and build project using below command
# "mvn clean package"
COPY /target/egov-filestore-0.0.1-SNAPSHOT.jar /opt/egov/egov-filestore-0.0.1-SNAPSHOT.jar

# launch standalone webapp
CMD ["java","-jar", "/opt/egov/egov-filestore-0.0.1-SNAPSHOT.jar"]
#CMD ["java", "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8787", "-jar", "/opt/egov/egov-filestore-0.0.1-SNAPSHOT.jar"]

# NOTE: the two 'RUN' commands can probably be combined inside of a single
# script (i.e. RUN build-and-install-app.sh) so that we can also clean up the
# extra files created during the `mvn package' command. that step inflates the
# resultant image by almost 1.0GB.