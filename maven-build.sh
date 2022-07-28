# #!/bin/bash

export TODO_APP_VERSION=2.0.0
export EDGE_SERVICE_VERSION=1.0.0
export CONFIG_SERVER_VERSION=1.0.0

# remove images if exists
docker rmi config-server:${CONFIG_SERVER_VERSION}
docker rmi edge-service:${EDGE_SERVICE_VERSION}
docker rmi todo:${TODO_APP_VERSION}

# config server
mvn -f config-server/ clean package -DskipTests
docker build --build-arg JAR_FILE=build/libs/config-server-${CONFIG_SERVER_VERSION}.jar --tag config-server:${CONFIG_SERVER_VERSION} -f config-server/Dockerfile config-server/

# edge service
mvn -f config-server/ clean package -DskipTests
docker build --build-arg JAR_FILE=build/libs/edge-service-${EDGE_SERVICE_VERSION}.jar --tag edge-service:${EDGE_SERVICE_VERSION} -f edge-service/Dockerfile edge-service/

# todo app
mvn -f config-server/ clean package -DskipTests
docker build --build-arg JAR_FILE=build/libs/todo-${TODO_APP_VERSION}.jar --tag todo:${TODO_APP_VERSION} -f todo-app/Dockerfile todo-app/

