# #!/bin/bash

export TODO_APP_VERSION=2.0.0
export EDGE_SERVICE_VERSION=1.0.0
export CONFIG_SERVER_VERSION=1.0.0

# remove images if exists
docker rmi config-server:${CONFIG_SERVER_VERSION}
docker rmi edge-service:${EDGE_SERVICE_VERSION}
docker rmi todo:${TODO_APP_VERSION}

# config server
config-server/gradlew clean build -p config-server -x test
docker build --build-arg JAR_FILE=build/libs/config-server-${CONFIG_SERVER_VERSION}.jar --tag config-server:${CONFIG_SERVER_VERSION} -f config-server/Dockerfile config-server/

# edge service
edge-service/gradlew clean build -p edge-service -x test
docker build --build-arg JAR_FILE=build/libs/edge-service-${EDGE_SERVICE_VERSION}.jar --tag edge-service:${EDGE_SERVICE_VERSION} -f edge-service/Dockerfile edge-service/

# todo app
todo-app/gradlew clean build -p todo-app -x test -x integration
docker build --build-arg JAR_FILE=build/libs/todo-${TODO_APP_VERSION}.jar --tag todo:${TODO_APP_VERSION} -f todo-app/Dockerfile todo-app/

