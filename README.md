# Task-Management-System
A simple task management system using Spring Boot, Java and PostgreSQL.

## Features
- User registration and login
- Update user roles (Only admin can update user roles)
- Create, update, delete and view tasks
- Search tasks by title, description, status, priority, due date and assignee
- Pagination and sorting on tasks
- Send email notifications to assignee when a task is due in 24 hours

## Technologies
- Spring Boot
- PostgreSQL
- Liquibase
- Lombok
- Swagger
- MailHog
- JUnit
- Mockito
- Gradle
- Docker
- Docker Compose
- TestContainers
- Quartz
- Spring Security

## Running the application
### Requirements
- Java 21.0.0
- Docker
- Docker Compose

### Steps
1. Open the project directory in your terminal
2. Build the application using the following command
    ```bash
    ./gradlew build
    ```
3. Run the following command to start the application
    ```bash
    ./gradlew bootRun
    ```
4. Open your browser and go to `http://localhost:8080/api/api-docs` to view the API documentation
5. Open your browser and go to `http://localhost:8080/api/api-docs/swagger-ui/index.html` to view the API documentation using Swagger UI
6. Open your browser and go to `http://localhost:8025/#` to view the MailHog UI

### Running the application using Docker
1. Open the project directory in your terminal
2. Edit compose.yml file and uncomment the following lines
    ```yaml
    #  app:
    #    build: .
    #    ports:
    #      - "8080:8080"
    #    environment:
    #      - spring.mail.host=mailhog
    #      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/task_management_system_db
    #      - SPRING_DATASOURCE_USERNAME=user
    #      - SPRING_DATASOURCE_PASSWORD=test@123
    #      - SPRING_JPA_HIBERNATE_DDL_AUTO=none
    #    depends_on:
    #      - DB
    #      - mailhog
    ```
3. Run the following command to start the application
    ```bash
    docker-compose up
    ```
4. Follow the steps 3, 4 and 5 from the previous section

### Additional Notes
* The application uses a default username and password for the admin user.
* The application sends email notifications using MailHog. You can configure a real email server in the application.properties file.
* The application uses Quartz Scheduler to send email notifications for upcoming due dates.

### Test Data
* The application uses Liquibase to insert test data into the database
* The application uses the following test data:
  * 2 users
    - admin
    ```text
        username: admin@abc.com
        password: pass123
    ```
    - user
    ```text
        username: user@abc.com
        password: pass123
    ```
  * 2 tasks
  * Tasks due date will be in 6 hours which will cause the application to send email notifications

### Running the tests
1. Open the project directory in your terminal
2. Run the following command to run the tests
    ```bash
    ./gradlew test
    ```
3. I have added both unit tests and integration tests
4. The application uses TestContainers to run integration tests
5. The application uses Mockito to mock the dependencies
6. The application uses JUnit to run the tests
7. The application uses Jacoco to generate test coverage reports

### Additional Notes
* The application uses Lombok to reduce boilerplate code
* The application uses Spring Security to secure the endpoints
* The application uses Gradle as the build tool
* The application uses Docker and Docker Compose to run the application and the database
* The application uses Swagger to document the API
* The application uses Liquibase to manage the database schema
* The application uses JUnit, Mockito and TestContainers to run the tests

### Future Improvements
* Add more tests to improve the test coverage
* Use Elasticsearch to store task title and description which will make it easier to search tasks at scale
* Use Redis to cache the frequently accessed data
* Use Grafana and Prometheus to monitor the application
* Use Kubernetes to deploy the application