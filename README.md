# micro-service-reference-project

This is an Undertow based server integrated with Spring and JaxRS.

This project is inspired by an idea to quickly create a production ready project with all the required infrastructure at low cost yet with important security measures in place and ability to quickly scale in order to ship a quality product to early adopters. Ideal for quickly starting an app to validate ideas and scale if needed.

Visit `localhost:8080/apidoc` to see the endpoints.

### 🌀 DB Migration
`mvn compile flyway:baseline; `
`mvn compile flyway:migrate; `
