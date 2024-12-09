FROM openjdk:11-slim
COPY ./target/jdbc*.jar /usr/local/app/jdbc/lib/jdbc.jar
COPY ./src/main/resources/properties.txt /usr/local/app/jdbc/lib/properties.txt
ENTRYPOINT ["java","-jar","/usr/local/app/jdbc/lib/jdbc.jar"]