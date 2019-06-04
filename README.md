This is the Processor of card application with CACHE(Implementation of Spring) second level system and Threads for better performance.

To run it locally:

- Create Package: mvn package

- Create image Docker:** docker build -t processor .

- Create and execute Container:** docker run --network host -d --name processor processor


Two card registered:
- 1234567890123456
- 4485617978182589

Port for transactions socket:
- 9004

Access DB:
 - URL: http://localhost:9005/h2
 - JDBC URL: jdbc:h2:mem:hubfintech
 - Username: root
 - Password: root

Swagger:
    http://localhost:9005/swagger-ui.html#