FROM openjdk:11-jre-slim

COPY target/invoices-0.0.1-SNAPSHOT.jar /app/invoices-0.0.1-SNAPSHOT.jar

COPY config.yml /app/config.yml

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "invoices-0.0.1-SNAPSHOT.jar" ,"server", "config.yml"]