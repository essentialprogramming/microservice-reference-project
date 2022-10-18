# Microservice Reference Project

This project is inspired by the idea to quickly create a production ready project with all the required infrastructure at low cost yet with important security measures in place and ability to quickly scale in order to ship a quality product to early adopters. Ideal for quickly starting an app to validate ideas and scale if needed. The implementation includes code samples for different features. It uses Spring as the framework of choice because it provides a nice set of convenience features when bootstrapping and plugging together the application.

 # :bookmark_tabs: Prerequisites:
By default, the project uses JDK 8, but will also work with JDK 11 and above.

* **JDK**
  - Oracle
    - Java : http://www.oracle.com/technetwork/java/javase/downloads/index.html
  - Adoptium
    - Java : https://adoptium.net/temurin/releases
 
  - RedHat
    - Java : https://developers.redhat.com/products/openjdk/download
* [Maven](https://maven.apache.org/)


---------------
# :bookmark: Build and run

Build
---------------
* Get the latest version from the git repository.
* Run ` mvn clean install` to build the project.


Run
---------------
#### 1. Using IntelliJ
Start the Server using  " Run `Server.main()` " command, selected from the dialog after right click on:
- Main Application class: `com.server.Server`

#### 2. Using CLI
To run the application from command line do following steps
- open `cmd` window
- change directory to the root of your microservice project
- run `mvn clean install` to create a jar-file of your microservice.
- call `java -jar essentialprogramming-api/target/uber-essentialprogramming-api-1.0.0-SNAPSHOT` from the console

Visit `localhost:8080/apidoc` to see the endpoints.

---------------
### 🌀 DB Migration
- Change directory to 'db-migration'
- Run
`mvn compile flyway:baseline; `
`mvn compile flyway:migrate; `


### 🌀 Developer Setup
#### Enable lombok

- https://projectlombok.org/setup/intellij
- Verify that annotation processing is enabled in Intellij (`File` -> `Settings` -> `Build, Execution, and Deployment`
  -> `Compiler` -> `Annotation Processers`)
  
## :sunglasses: Usefull commands
    `mvn dependency:tree`
 
    `mvn dependency:tree -Dincludes=net.minidev:json-smart` //Filter the dependency tree
    
     [depgraph-maven-plugin](https://github.com/ferstl/depgraph-maven-plugin) :
    `mvn com.github.ferstl:depgraph-maven-plugin:3.3.0:graph -DrepeatTransitiveDependenciesInTextGraph -DshowVersions -DgraphFormat=text -DshowGroupIds -DshowConflicts -DshowDuplicates`
    
   
# :mortar_board: Learning Resources JPA (Hibernate) 
  - JPA(Hibernate) limits: [Is your query too complex for JPA and Hibernate ?](https://thorben-janssen.com/query-complex-jpa-hibernate/#1_Use_subqueries_outside_of_WHERE_and_HAVING_clauses)
  - Native Queries
    - Defining and executing native queries
       - https://thorben-janssen.com/jpa-native-queries/#Createnbspad-hoc_native_queries
    - How to return DTOs from native queries 
       - https://thorben-janssen.com/spring-data-jpa-dto-native-queries/
   
  - Spring Data JPA
    - DTO projections : https://vladmihalcea.com/spring-jpa-dto-projection/
    
  - Hibernate 6 
    - Hibernate 6 and JPQL Window Functions: https://vladmihalcea.com/hibernate-jpql-window-functions/
    
    
    
  - JOOQ
    - Hibernate & JOOQ interation: 
       -  https://thorben-janssen.com/hibernate-jooq-a-match-made-in-heaven/ 
       -  https://thorben-janssen.com/hibernate-tip-how-to-integrate-an-external-query-builder/                
    - Window functions 
      - https://blog.jooq.org/probably-the-coolest-sql-feature-window-functions/
      -  https://blog.jooq.org/the-difference-between-row_number-rank-and-dense_rank/
    - TOP N Queries 
      - https://blog.jooq.org/how-to-write-efficient-top-n-queries-in-sql/

    - DTO projections : https://blog.jooq.org/the-second-best-way-to-fetch-a-spring-data-jpa-dto-projection/
    
 - Blaze Persistence – The Best Way to Write JPA Criteria Queries
      - https://persistence.blazebit.com/documentation/1.6/core/manual/en_US/index.html
      - https://vladmihalcea.com/blaze-persistence-jpa-criteria-queries/
      
 - SQL RANK functions: https://www.sqlshack.com/overview-of-sql-rank-functions/
 - WINDOW functions:   https://docs.snowflake.com/en/sql-reference/functions-analytic.html
   
    
    
### :nut_and_bolt: Utilities
    
    ### 🌀 WSL 2
    https://www.freecodecamp.org/news/how-to-install-wsl2-windows-subsystem-for-linux-2-on-windows-10/
    
    ### 🌀 Java Decompiler
    http://java-decompiler.github.io/
    
    
[:top: Back to Top](#microservice-reference-project)
 
