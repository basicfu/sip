FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar app.jar
CMD ["/bin/bash"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]