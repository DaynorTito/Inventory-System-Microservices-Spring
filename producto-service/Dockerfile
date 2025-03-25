FROM amazoncorretto:21-alpine AS builder

RUN apk add --no-cache maven

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src/ ./src/

RUN mvn package -DskipTests

FROM amazoncorretto:21-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/inventory_system
ENV SPRING_DATASOURCE_USERNAME=db_user
ENV SPRING_DATASOURCE_PASSWORD=db_password
ENV SPRING_PROFILES_ACTIVE=prod
ENV EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8761/eureka

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
