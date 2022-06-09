# Getting Started

### Project
Project consists of 2 gradle modules and can be imported by Intellij as Gradle project
* **savings** module - app responsible for running specific account, the name of the account can be specified in application.yml or via environment. Please see example configuration.
* **api-gw** module - API Gateway that is responsible for routing traffic to service that runs specific account. It can be configured via application.yml or environment variables.

You can just compile and run apps in development mode in your IDE. Before starting savings app, please don't forget to start **db** service in docker/docker-compose.yml

You can run the simple test configuration right away with the following commands:

    ./gradlew jibDockerBuild
    docker-compose -f ./docker/docker-compose.yml


This will spin 4 containers:
- PostgreSQL database
- api-gw - gateway
- savings-a - savings service responsible for the account **a**
- savings-b - savings service responsible for the account **b**

