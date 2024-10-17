FROM amazoncorretto:17.0.12

WORKDIR /app

COPY versions/crm-system-0.0.1-SNAPSHOT.jar app.jar

# Установка переменных окружения
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/your_database
ENV SPRING_DATASOURCE_USERNAME=your_username
ENV SPRING_DATASOURCE_PASSWORD=your_password

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]