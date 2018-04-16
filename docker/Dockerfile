FROM openjdk:9-jdk-slim
MAINTAINER Artur Tenorio"arturvt@gmail.com"
ADD ./build/libs/core.jar /app/kalah-game
ENTRYPOINT ["java"]
CMD ["-jar", "/app/kalah-game"]
EXPOSE 8080
