# TODO APP
[![Gradle Pipeline](https://github.com/Raghav2211/spring-web-flux-todo-app/actions/workflows/gradle-pipeline.yml/badge.svg)](https://github.com/Raghav2211/spring-web-flux-todo-app/actions/workflows/gradle-pipeline.yml)
[![Maven Pipeline](https://github.com/Raghav2211/spring-web-flux-todo-app/actions/workflows/maven-pipeline.yml/badge.svg)](https://github.com/Raghav2211/spring-web-flux-todo-app/actions/workflows/maven-pipeline.yml)

This application is an scaffold which is a basic setup of spring-cloud-gateway, config-server and a resource server(A todo application). It demonstrate how data flows between gateway & resource-server using config-server which holds specific enviornment configs and secrets 

## WorkFlow
![Workflow](documentation/workflow.png)

## Tech
TODO App uses following technologies:

* [Java 11] - JDK
* [Maven-3.6.3] - Build tool
* [Gradle-6.7.1] - Build tool
* [Springboot-2.7.0] - Open source Java-based Micro-Service framework
* [OpenApi-1.6.9] - Rest API documentation
* [Junit-5.7.0] - Unit test
* [MongoDB-4.2.21] - Backend data store
* [Docker-19.03.8] - OS level virtualization

## Build ##
1. Todo App
   [Build](./todo-app/README.md)
2. Edge Service
   [Build](./edge-service/README.md)
3. Config Server
   [Build](./config-server/README.md)   
    


## Deploy ##
 
[Todo Infra](https://github.com/Raghav2211/todo-app-infra)
