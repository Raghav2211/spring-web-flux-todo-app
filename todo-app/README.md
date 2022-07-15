# Todo App #
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
export TODO_APP_VERSION=<version>
```

```bash
docker pull ghcr.io/raghav2211/spring-web-flux-todo-app/todo:${TODO_APP_VERSION}
```
or

```bash
docker build --build-arg JAR_FILE=target/todo-${TODO_APP_VERSION}.jar --tag todo:${TODO_APP_VERSION} .
```
If you use Gradle, you can run it with the following command

```bash
docker build --build-arg JAR_FILE=build/libs/todo-${TODO_APP_VERSION}.jar --tag todo:${TODO_APP_VERSION} .
```

#### Pushing Image to ECR ####

```bash
$ export AWS_REGION=<aws.region>
$ export AWS_ACCOUNT_ID=<aws.account.id>
# create if not exists
$ aws ecr create-repository --repository-name todo 
$ aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
$ docker tag todo:${TODO_APP_VERSION} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/todo:${TODO_APP_VERSION}
$ docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/todo:${TODO_APP_VERSION}
```