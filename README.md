# Emplyees Backend API

## How to Run

### Prerequisites
1. JDK 17

### How to run?

#### Starting the project outside of an IDE
You can start the project by navigating to the main folder of the project itself and then executing the following command: 

```console
./gradlew bootRun --args='--spring.profiles.active=dev
```


## CSV Parser Date Format
By default the parser uses the Mysql date format (yyyy-MM-dd). You can change the format by changing the value of APP_CSV_DATE_FORMAT in src/main/resources/application.yaml or by providing value of an environment variable with the same name.

## Allowed Origins for CORS
By default CORS is allowed for http://localhost:3000 (assuming the frontend is executed in a dev environment). You can change the allowed origins by providing string value for the APP_CORS_ALLOWED_ORIGINS variable in src/main/resources/application.yaml (if you need to provide more than 1 origin, then please make sure the string value is comma delimiting all possible origins)

## About the Project Structure
This project uses [multitiered architecture](https://en.wikipedia.org/wiki/Multitier_architecture).
It consists of Presentation and Application Layer (web folder), Business Logic Layer (bl folder) and Data Access Layer (dal)

## TODO
- Implement the missing tests (unit and integration)
