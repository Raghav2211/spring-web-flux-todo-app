# TODO APP
[![Build](https://github.com/Raghav2211/spring-web-flux-todo-app/actions/workflows/build.yml/badge.svg)](https://github.com/Raghav2211/spring-web-flux-todo-app/actions/workflows/build.yml)

Todo app is an example rest application, which can be build and deployed on Local and AWS environment.

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
   ##### Maven #####
    Build application using below command
    ```bash
      # Go to folder
      cd ./todo-app
    ```
    ```bash
    mvn clean install
    ```
    or
    
    ```bash
    ./mvnw clean install
    ```
    
    ##### Gradle #####
    Build application using below command
    
    ```bash
    gradle clean build
    ```
    or
    
    ```bash
    ./gradlew clean build
    ```
    ##### Docker image #####
    
    ```bash
    docker pull ghcr.io/raghav2211/spring-web-flux-todo-app/todo:2.0.0
    ```
    or
    
    ```bash
    
    docker build --tag todo:2.0.0 .
    ```
    If you use Gradle, you can run it with the following command
    
    ```bash
    docker build --build-arg JAR_FILE=build/libs/\*.jar --tag todo:2.0.0 .
    ```
2. Edge Service
   ```bash
      # Go to folder
      cd ./edge-service
    ```
   ##### Maven #####
   Build application using below command

    ```bash
    mvn clean install
    ```
   or

    ```bash
    ./mvnw clean install
    ``` 
   
   ##### Gradle #####
   Build application using below command

    ```bash
    gradle clean build
    ```
   or

    ```bash
    ./gradlew clean build
    ```
   ##### Docker image #####

    ```bash
    docker pull ghcr.io/raghav2211/spring-web-flux-todo-app/edge-service:2.0.0
    ```
   or

    ```bash
    
    docker build --tag edge-service:2.0.0 .
    ```
   If you use Gradle, you can run it with the following command

    ```bash
    docker build --build-arg JAR_FILE=build/libs/\*.jar --tag edge-service:2.0.0 .
    ```
    


## Deploy ##
 
[Todo Infra](https://github.com/Raghav2211/todo-app-infra)
