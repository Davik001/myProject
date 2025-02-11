FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY target/myProject-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV DB_HOST=database
ENV DB_PORT=5432
ENV DB_NAME=chief
ENV DB_USER=chief
ENV DB_PASSWORD=root
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "app.jar"]
