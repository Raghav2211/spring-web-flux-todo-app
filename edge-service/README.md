# Edge Service #

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