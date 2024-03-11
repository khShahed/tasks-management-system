FROM amazoncorretto:21

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080 9464

ENTRYPOINT ["java", "-jar", "app.jar"]