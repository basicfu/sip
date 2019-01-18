FROM registry.cn-beijing.aliyuncs.com/basicfu/openjdk:8-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar app.jar
CMD ["/bin/bash"]
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
