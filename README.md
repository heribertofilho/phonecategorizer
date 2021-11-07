# Phone Categorizer
A customer phone management tool
### Instructions
1. Build the application by ./gradlew clean build
2. Run the command docker-compose up
3. Open your browser and take a look at this endpoint: http://localhost:8080/api/swagger-ui/
### How to use
To run this application you can just issue a curl command inside your terminal of preference, also you have some
parameters.

- Optional parameters:
  1. country ENUM: MOROCCO, CAMEROON, ETHIOPIA, UGANDA and MOZAMBIQUE.
  2. onlyValidPhones BOOLEAN: displays only phone number that are valid
  3. page INT: set the page that will be returned of the database
  4. size INT: limits number of elements returned on a page from the database

- Example:
```console
curl --location --request GET 'http://localhost:8080/api/customer'
```
- Example with Parameters:
```console
curl --location --request GET 'http://localhost:8080/api/customer?country=MOZAMBIQUE&onlyValidPhones=false&page=0&size=20'
```
### Integration Tests
To run integration tests issue this command in your terminal:
```groovy
gradle :integrationTests
```
### Bonus features:
- Swagger
- Docker