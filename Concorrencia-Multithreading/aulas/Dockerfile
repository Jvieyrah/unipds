FROM eclipse-temurin:21-alpine
LABEL Authors="joao"

WORKDIR /app
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]