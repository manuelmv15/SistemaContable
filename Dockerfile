# syntax=docker/dockerfile:1.7

# ====== Build ======
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia solo el pom primero para maximizar caché de capas
COPY pom.xml .

# Resolución mínima de dependencias (evita go-offline)
# - Usa cache mount para ~/.m2 y paralelismo 1 hilo/CPU
ENV MAVEN_OPTS="-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn"
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    mvn -q -B -T 1C -DskipTests dependency:resolve

# Ahora sí, copia el código y empaqueta
COPY src ./src
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    mvn -q -B -T 1C -DskipTests clean package

# ====== Runtime ======
# Usa Temurin JRE para que tengas shell si lo necesitas
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el jar resultante
COPY --from=build /app/target/*.jar /app/app.jar

# Opcionales
ENV SPRING_PROFILES_ACTIVE=docker \
    JAVA_OPTS=""

EXPOSE 8090
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
