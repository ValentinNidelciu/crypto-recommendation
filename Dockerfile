FROM eclipse-temurin:17-jdk-jammy
WORKDIR /opt
ENV PORT 8080
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar