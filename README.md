This is the Processor of card application.

To run it locally:

- Create Package: mvn package

- Create image Docker:** sudo docker build -t hubfintech .

- Create and execute Container:** sudo docker run --network host -d --name hubfintech hubfintech


Two card registered:
- 1234567890123456
- 4485617978182589

Port for transactions socket:
- 9004

Access DB:
 - URL: http://localhost:9005/h2
 - JDBC URL: jdbc:h2:mem:hubfintech
 - Username: admin
 - Password: admin
