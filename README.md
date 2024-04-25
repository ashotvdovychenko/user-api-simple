# Users API

## About project
Test assignment. REST API based on the web Spring Boot application.\
API for manipulating with users information.

It has the following functionality:
- Create user. Allowed to register users who are more than 18 years old. The value [18] is taken from properties file.
- update one/some user fields
- Update all user fields
- Delete user
- Search for users by birth date range.

Project is covered with integration tests for controller and unit tests for service.

## Database
This project doesn`t use real database.\
Instead of DB it uses in-memory HashMap storage.

## Swagger(OpenAPI 3)

After running application, the Swagger UI page is available at http://localhost:8080/swagger-ui.html