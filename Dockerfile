FROM openjdk:25-ea-11-jdk-bookworm

WORKDIR /user-app

COPY target/notification-service-0.0.1-SNAPSHOT.jar user-app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/user-app/user-app.jar"]