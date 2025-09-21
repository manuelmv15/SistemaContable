# ====== Build stage ======
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia solo lo mínimo para cachear dependencias
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw
RUN ./mvnw -q -B -DskipTests dependency:go-offline

# Ahora copia el código y construye
COPY src src
RUN ./mvnw -q -B -DskipTests package

# ====== Run stage (imagen liviana) ======
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copia el jar construido
COPY --from=build /app/target/*.jar app.jar

# Opcional: variables para la JVM
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
