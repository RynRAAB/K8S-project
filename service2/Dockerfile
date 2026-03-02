FROM eclipse-temurin:21-jdk
VOLUME /tmp
EXPOSE 8081
ADD target/simple-springboot-webapp-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]