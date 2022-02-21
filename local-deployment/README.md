# How to execute the local deployment

## Prerequisite

Docker must be installed and running.

## Steps

1. Make sure you execute `mvn clean install` on root pom level at least once.
2. Go into "localdeployment" and execute `docker-compose up`. It
   will start a postgresql container. As an alternative you can
   execute `docker-compose -f localdeployment/docker-compose.yml up` from the root pom level.
3. Execute `mvn compile flyway:baseline  -f db-migration` and `mvn compile flyway:migrate  -f db-migration` to setup the database
4. Start the Server using  " Run `Server.main()` " command in Intellij.

