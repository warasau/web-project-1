# Lab Work 1-2: Introduction to Spring Boot

## Overview

This is a Spring Boot application for managing library operations including books, authors, readers, and fines.

## Technologies Used

*   **Java 21+**
*   **Spring Boot 3.5.6**
*   **Apache Maven**
*   **Lombok**

## Endpoints

The application exposes the following two endpoints:

*   `GET /`
    *   Displays a welcome message and a brief description of the project.
*   `GET /say/{message}`
    *   Displays a custom message provided in the URL path.
    *   Example: `http://localhost:8081/say/hello-world`

## API Endpoints

### Book Management
*   `GET /api/books`
    *   Get all books
*   `GET /api/books/{id}`
    *   Get book by ID
*   `POST /api/books`
    *   Create new book
*   `PUT /api/books/{id}`
    *   Update book
*   `DELETE /api/books/{id}`
    *   Delete book

### Author Management
*   `GET /api/authors`
    *   Get all authors
*   `GET /api/authors/{id}`
    *   Get author by ID
*   `POST /api/authors`
    *   Create new author
*   `PUT /api/authors/{id}`
    *   Update author
*   `DELETE /api/authors/{id}`
    *   Delete author

### Reader Management
*   `GET /api/readers`
    *   Get all readers
*   `GET /api/readers/{id}`
    *   Get reader by ID
*   `POST /api/readers`
    *   Create new reader
*   `PUT /api/readers/{id}`
    *   Update reader
*   `DELETE /api/readers/{id}`
    *   Delete reader

### Fine Management
*   `GET /api/fines`
    *   Get all fines
*   `GET /api/fines/{id}`
    *   Get fine by ID
*   `POST /api/fines`
    *   Create new fine
*   `PUT /api/fines/{id}`
    *   Update fine
*   `DELETE /api/fines/{id}`
    *   Delete fine

The application will start on `http://localhost:8081` (or the port specified in `application.properties`).