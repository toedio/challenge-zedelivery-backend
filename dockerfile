FROM openjdk:8u212-jre-alpine
VOLUME /home
ADD target/backendchallenge-0.0.1.jar backendchallenge.jar
ENTRYPOINT [ "sh", "-c", "java -jar /backendchallenge.jar --spring.profiles.active=prd" ]