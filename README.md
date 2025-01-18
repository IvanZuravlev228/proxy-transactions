# Proxy Transactions

## About the Project

This project acts as a proxy for the `api.spending.gov.ua` API.

This project allows you to retrieve transactions, but in limited quantities, as the amount of data retrieved may exceed what a browser can handle. The project also provides the ability to store an unlimited amount of data in the database.

## How to Run

1. Create a database with the following command:
   ```sql
   CREATE DATABASE `transaction-proxy`;
2. Change the root password in <code>application.properties</code>
3. Run the following command to build the project:
   ```shel
   mvn package
4. Finally, run the application with the following command:
   ```shel
   java -Xmx512m -jar target/proxy-0.0.1-SNAPSHOT.jar
5. For testing use **[Postman collection](https://www.postman.com/maintenance-operator-97553329/transaction-proxy/collection/32177921-d2e754ca-4549-47a4-9ab0-1b438dac023c?action=share&creator=32177921)**
