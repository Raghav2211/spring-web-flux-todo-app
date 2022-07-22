# Config Server #
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
export CONFIG_SERVER_VERSION=<version>
```

```bash
docker pull ghcr.io/raghav2211/spring-web-flux-todo-app/config-server:${CONFIG_SERVER_VERSION}
```
or

```bash
docker build --build-arg JAR_FILE=build/libs/config-server-${CONFIG_SERVER_VERSION}.jar --tag config-server:${CONFIG_SERVER_VERSION} .
```

#### Pushing Image to ECR ####

```bash
$ export AWS_REGION=<aws.region>
$ export AWS_ACCOUNT_ID=<aws.account.id>
# create if not exists
$ aws ecr create-repository --repository-name config-server 
$ aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
$ docker tag config-server:${CONFIG_SERVER_VERSION} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/config-server:${CONFIG_SERVER_VERSION}
$ docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/config-server:${CONFIG_SERVER_VERSION}
```