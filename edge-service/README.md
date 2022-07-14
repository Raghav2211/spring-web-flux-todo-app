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
export EDGE_SERVICE_VERSION=<version>
```


 ```bash
 docker pull ghcr.io/raghav2211/spring-web-flux-todo-app/edge-service:${EDGE_SERVICE_VERSION}
 ```
or

 ```bash
 
 docker build --build-arg JAR_FILE=target/edge-service-${EDGE_SERVICE_VERSION}.jar --tag edge-service:${EDGE_SERVICE_VERSION} .
 ```
If you use Gradle, you can run it with the following command

 ```bash
 docker build --build-arg JAR_FILE=build/libs/edge-service-${EDGE_SERVICE_VERSION}.jar --tag edge-service:${EDGE_SERVICE_VERSION} .
 ```

#### Pushing Image to ECR ####

```bash
$ export AWS_REGION=<aws.region>
$ export AWS_ACCOUNT_ID=<aws.account.id>
$ aws ecr create-repository --repository-name edge-service 
$ aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
$ docker tag edge-service:${EDGE_SERVICE_VERSION} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/edge-service:${EDGE_SERVICE_VERSION}
$ docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/edge-service:${EDGE_SERVICE_VERSION}
```