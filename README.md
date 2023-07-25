# Loan Application API

The Loan Application API allows authenticated users to go through a loan application process and manage their loan repayments. It provides endpoints to create a loan request, approve the loan, view loans for a specific user, and add loan repayments.

## Prerequisites

Before running the application, ensure you have the following installed:

- Java Development Kit (JDK) 11 or higher
- Gradle
- MySQL Server

## Setup

1. Clone the repository:

```bash
git clone <repository_url>
cd loan-application-api

Database Configuration:

Create a new MySQL database for the application.

Update the database configuration in src/main/resources/application.properties as follows, keep everything else the same:
spring.datasource.url=jdbc:mysql://localhost:3306/<your_database_name>
spring.datasource.username=<your_database_username>
spring.datasource.password=<your_database_password>
```

2. Build The application:
```
./gradlew clean build
```

Running the Application

Once the application is built, you can run it using the following command:
```
./gradlew bootrun
```
The application will start and be accessible at http://localhost:7012.


API Endpoints

All API endpoints are shared in APIs file within resources folder

API Endpoints
The following API endpoints are available:

1. Create a User:

* Method: POST
* URL: http://localhost:7012/api/users/upsert
* Request Body: JSON format with "name","email_id," and "address" fields.
* Example Request:
``` 
    curl --location 'localhost:7012/api/users/upsert' \
       --header 'Content-Type: application/json' \
       --data-raw '{
       "name": "prakash iyer",
       "address": "anonymous street",
       "email_id": "prakash@aspire.net"
       }'
   ```
   

2. Create a new loan request:
*   Method: POST
*   URL: http://localhost:8080/api/loans/create
*   Request Body: JSON format with "amountRequired","user_id" and "loanTerm" fields.
*   Example Request:
   ```
    curl --location 'localhost:7012/api/loans/create' \
   --header 'Content-Type: application/json' \
   --data '{
   "loan_term": 4,
   "amount_required": 50000,
   "user_id": "88f06d1c-3711-4db7-ac57-ab52c2a0b133"
   }'
   ```

3. View Loans for a user:
* Method: GET
* URL: http://localhost:8080/api/loans/get
*   HEADER: set X-USER-ID as key & user_id as value.
*   Example Request:
   ```
    curl --location 'localhost:7012/api/loans/get' \
   --header 'X-USER-ID: 88f06d1c-3711-4db7-ac57-ab52c2a0b133'
```

4. Approve a Loan :
* Method: POST
* URL: http://localhost:8080/api/loans/approve
* HEADER: loan ID received from the create loan response with key 'LOAN-ID''.
* Example Request:
   ```
  curl --location --request POST 'localhost:7012/api/admin/loans/approve' \
   --header 'LOAN-ID: 65dd3df1-2b5d-4153-b0ce-76a10854afef' \
   --data ''
  ```

5. Add a repayment:
* Method: POST
* URL: http://localhost:8080/api/loans/repayments
* Request Body: JSON format with "amount" and "loan_id" fields.
* Example Request:
   ```
  curl --location 'localhost:7012/api/loans/repayment' \
   --header 'Content-Type: application/json' \
   --data '{
   "loan_id": "65dd3df1-2b5d-4153-b0ce-76a10854afef",
   "amount": 12500
   }'
  ```

