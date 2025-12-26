# multi-stage build for Spring Boot application
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY lint.gradle .
COPY checkstyle.xml .
COPY pmd-ruleset.xml .
COPY spotbugs-filter.xml .

# copy source code
COPY src src

# build the application
RUN chmod +x gradlew && \
    ./gradlew bootJar --no-daemon && \
    find build/libs -name '*.jar' -not -name '*-plain.jar' -exec mv {} app.jar \;

# runtime stage
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="matchmycoffee"
LABEL description="MatchMyCoffee Backend Service"

WORKDIR /app

# create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# copy JAR from builder stage
COPY --from=builder /app/app.jar app.jar

# change ownership to non-root user
RUN chown -R appuser:appgroup /app

# switch to non-root user
USER appuser

# expose port
EXPOSE 8080

# health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# jvm options for container
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

# run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
