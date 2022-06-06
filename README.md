# TODO APP

Todo app is an example rest application, which can be build and deployed on Local and AWS environment.

## Tech

TODO App uses following technologies:

* [Java 11] - JDK
* [Maven-3.6.3] - Build tool
* [Gradle-6.7.1] - Build tool
* [Springboot-2.6.1] - Open source Java-based Micro-Service framework
* [Swagger-3.0.0] - Rest API documentation
* [Junit-5.7.0] - Unit test
* [MySql-8.0.22] - Backend data store
* [Docker-19.03.8] - OS level virtualization

## Build ##
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

docker build --tag todo:1.0.0 .
```
If you use Gradle, you can run it with the following command

```bash
docker build --build-arg JAR_FILE=build/libs/\*.jar --tag todo:1.0.0 .
```

## Deploy ##

   - Install
  
        ```bash
        docker-compose --env-file=env/<env>/Docker.env up -d
        #or 
        # Override default docker-compose configuration
        docker-compose --env-file=env/<env>/Docker.env -f docker-compose.yaml -f env/<env>/docker-compose-override.yml up -d
        ```
   - Uninstall
  
        ```bash
        docker-compose down
        ```     
        
## Configuration ## 

The following table lists the configurable parameters of the TodoApp swarm cluster and their default values.

  Parameter | Description | Default
  --- | --- | ---
  `TODO_IMAGE_TAG` | Image tag for Todo-App | `1.0.0`
  `BASIC_AUTH_ENABLE` | Enable spring Basic-Auth | `false`        
  `BASIC_AUTH_USERNAME` | Username of Basic-Auth | ``                    
  `BASIC_AUTH_PASSWORD` | Password of Basic-Auth | ``                            
  `MYSQL_IMAGE_TAG` | Image tag for Mysql | `8.0.22`                                
  `MYSQL_USER` | Username of new user to create | ``        
  `MYSQL_PASSWORD` | Password for the new user | ``            
  `MYSQL_ROOT_PASSWORD` | Password for the root user | ``              
  `MYSQL_DATABASE` | Name for new database to create | ``                
  `MYSQL_DATA_SRC_PATH` | Host path for persistence mysql data | ``                    