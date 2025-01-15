# Invoice Assessment

## How to start the invoice application using Docker

1. Run `mvn clean install` to build your application.
2. Make necessary changes in `docker-compose.yml` to connect to your DB.
3. Run `docker-compose up --build` to run your Java application with PostgreSQL.

## How to start the invoice application without Docker

1. Run `mvn clean install` to build your application.
2. Change `jdbc:postgresql://db:5432/invoices` to `jdbc:postgresql://localhost:5432/invoices`.
3. Start the application with: `java -jar target/invoice-0.0.1-SNAPSHOT.jar server config.yml`

## Endpoints
/invoices - POST
Insert new invoice into Invoices table.

/invoices - GET
Retrieves all the data from the Invoices table.

/invoices/{id}/payments - POST
Paid amount is updated based on the amount that needs to be paid.

/invoices/process-overdue - POST
Processes all the overdue invoices based on today's date and inserts new data for each overdue invoice.

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
