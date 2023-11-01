FROM openjdk:11

WORKDIR /app

COPY apis/target/apis-1.0-SNAPSHOT-jar-with-dependencies.jar /app/apis-1.0-SNAPSHOT-jar-with-dependencies.jar

EXPOSE 8080

CMD ["sh", "-c", "java -jar apis-1.0-SNAPSHOT-jar-with-dependencies.jar"]
