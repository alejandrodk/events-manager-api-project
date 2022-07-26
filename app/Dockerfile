ARG APP_ROOT="/usr/app"

FROM maven:3.8.6-openjdk-18

ARG APP_ROOT
COPY . $APP_ROOT

WORKDIR $APP_ROOT

RUN mvn clean package

ARG JAR_FILE=$APP_ROOT/target/events-manager-api-boot.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", ${JAR_FILE}]