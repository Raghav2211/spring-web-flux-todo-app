# #!/bin/bash

# clear all dangling image(s) 
docker rmi $(docker images -f "dangling=true" -q)

export TODO_APP_VERSION=2.0.0
export EDGE_SERVICE_VERSION=1.0.0
export CONFIG_SERVER_VERSION=1.0.0

# config server
mvn -f config-server/ clean package -DskipTests
docker build --build-arg JAR_FILE=target/config-server-${CONFIG_SERVER_VERSION}.jar --tag config-server:${CONFIG_SERVER_VERSION} -f config-server/Dockerfile config-server/

# edge service
mvn -f edge-service/ clean package -DskipTests
docker build --build-arg JAR_FILE=target/edge-service-${EDGE_SERVICE_VERSION}.jar --tag edge-service:${EDGE_SERVICE_VERSION} -f edge-service/Dockerfile edge-service/

# todo app
mvn -f todo-app/ clean package -DskipTests
docker build --build-arg JAR_FILE=target/todo-${TODO_APP_VERSION}.jar --tag todo:${TODO_APP_VERSION} -f todo-app/Dockerfile todo-app/


