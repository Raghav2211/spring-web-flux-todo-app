FROM openjdk:11-jre-slim
LABEL org.opencontainers.image.source="https://github.com/raghav2211/spring-web-flux-todo-app"
ARG USER=todo
ARG GROUP=todo
ARG UID=1000
ARG GID=1000
ARG JAR_FILE=target/*.jar
RUN groupadd -g ${GID} ${GROUP} && useradd -u ${UID} -g ${GROUP} ${USER}
USER ${USER}:${GROUP}
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-server", "-Dfile.encoding=utf-8", "-XX:+ExitOnOutOfMemoryError", "-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=UTC","-jar","/app.jar"]