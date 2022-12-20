FROM gradle:jdk19-focal AS gradle_build
MAINTAINER Alessio Saltarin <alessiosaltarin@gmail.com>

# Copy source code
COPY build.gradle /build/
COPY src /build/src/

WORKDIR /build/
RUN gradle build
RUN ls -al build/libs

FROM eclipse-temurin:19-jdk-focal

# Temp volume to save Tomcat temp files
VOLUME /tmp
# Copy the jar file
WORKDIR /app
COPY --from=gradle_build /build/build/libs/build.jar /app/app.jar
RUN ls -al /app

# Entry point
ENTRYPOINT ["java","-jar","app.jar"]
