FROM amazoncorretto:17-alpine
WORKDIR /app
COPY build/libs/*.jar hackthon-postech.jar
ENTRYPOINT ["java", "-jar", "./hackthon-postech.jar"]