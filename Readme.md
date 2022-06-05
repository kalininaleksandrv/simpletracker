### general project and tech-stack info
* spring-boot with rest controllers and jpa as a general core
* spring-actuator available on `http://localhost:8080/actuator`
* h2 embedded as db (separate on a default and test environments)
* Flyway migrations on default scheme to create and populate DB, which more stable
  and less error-prone than hibernate auto generation
* lombok for less boilerplate and more readable code
* openapi `http://localhost:8080/api-docs/` and swagger for auto-describe endpoints 
* Junit5 for testing (only IssueController covered by tests as it's an example project)
* in a current implementation there is a naive planing approach based on idea,
that if we're plane shortest stories first, then we could fit maximum stories on 
each developer and week (surely in a many kind of incoming configurations of issues
this approach won't be effective), more effective approach may implement fore example 
graph of stories fore recursively build max-to-10-points branches of issues

### todo
on MVP stage project needs to following priority improvements
* improve planning algorithm
* add test coverage
* cover all API with OpenApi
* separate default (PROD) and DEV environment
* replace H2 with production-level DB (f.e. PostgreSQL),
  on DEV we could use containerized PostgreSQL, on TEST - I would recommend "TestContainers"
* add logging (f.e. logstash + MDC with a general filter approach)
* set up actuator carefully, integrate with monitoring
* secure API
* containerize (add Docker file, maven plugin etc.)

### to build and start project
* move to root directory
* build project with maven `mvn clean install`
* move to target directory `cd target`
* start spring-boot service `java -jar simpletracker-0.0.1-SNAPSHOT.jar`

### to run requests
#### db general info
service use embedded H2 with console available on `http://localhost:8080/h2-console/`
credentials contains in application.yaml, no password required
```
url: jdbc:h2:mem:db;DB_CLOSE_DELAY=-1
username: sa
password:
```

#### requests general info
service starts on `http://localhost:8080/` and there is 3 new developers already in DB there is a couple of ways to perform requests
* use Postman tool and postman collection which contains in `simpletracker/postman` 
* perform curl requests (see examples below)
* use OpenApi (only for story-management) on `http://localhost:8080/swagger-ui.html`


#### request scenario with curl
* add new developer
```
curl --location --request POST 'http://localhost:8080/api/v1/developer' \
--header 'Content-Type: application/json' \
--data-raw '{
"name" : "dev2"
}'
```
* update developer by id
```
curl --location --request PUT 'http://localhost:8080/api/v1/developer/1' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "John Johanson"
}'
```

* add new bug
```
curl --location --request POST 'http://localhost:8080/api/v1/issue' \
--header 'Content-Type: application/json' \
--data-raw '{
"title" : "bug1",
"issueType" : "BUG", 
"description": "bug_to_fix_1", 
"dateTime" : null, 
"developer" : {
        "id": 1
    },
"points": "",
"bugPriority" : "MAJOR"
}'
```

* add new story (reply multiple times to understand how's plan generator works)
```
curl --location --request POST 'http://localhost:8080/api/v1/issue' \
--header 'Content-Type: application/json' \
--data-raw '{
"title" : "story1",
"issueType" : "STORY", 
"description": "add feature1",
"points": 9
}'
```

* get plan
```
curl --location --request GET 'http://localhost:8080/api/v1/plan'
```
