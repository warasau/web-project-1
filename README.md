# Lab Work 1: Introduction to Spring Boot

## Endpoints

The application exposes the following two endpoints:

*   `GET /`
    *   Displays a welcome message and a brief description of the project.
*   `GET /say/{message}`
    *   Displays a custom message provided in the URL path.
    *   Example: `http://localhost:8081/say/hello-world`

## Technologies Used

*   **Java 21+**
*   **Spring Boot 3.5.6**
*   **Apache Maven**

The application will start on `http://localhost:8081` (or the port specified in `application.properties`).