# Lab Work 1-4: Introduction to Spring Boot

## Overview

A web application built with Spring Boot to automate core library operations. The system manages books, authors, readers, and fines, and includes key business logic like borrowing and returning books.
- Core Entities: Book, Author, Reader, Fine.
- Architecture: Classic three-tier (Controller, Service, Repository).
- Database: PostgreSQL, managed via Docker Compose.

## Technologies Used

* **Java 21**
* **Spring Boot 3.x**
* **Spring Data JPA (Hibernate)**
* **PostgreSQL**
* **Docker Compose**
* **Maven**

## Getting Started
   
1. Configure Environment 
- Create a .env file in the project root:

```env
   # PostgreSQL Credentials
   DB_USER={YOUR_USERNAME}
   DB_PASSWORD={YOUR_PASSWORD}
   DB_NAME={YOUR_DB_NAME}
   
   ADMIN_USERNAME=admin
   ADMIN_PASSWORD=Qwe123!@
```
   
2. Run the Database
```bash
   docker-compose up -d
```

3. Run the Application
```bash
   mvn spring-boot:run
```
The application will be available at http://localhost:8081.

## Endpoints

### Lab1:
*   `GET /`
    *   Displays a welcome message and a brief description of the project.
*   `GET /say/{message}`
    *   Displays a custom message provided in the URL path.
    *   Example: `http://localhost:8081/say/hello-world`

### Lab4:
#### Security and User Management
New Core Entity: User
*   Represents a system user with a username, hashed password, and a set of roles (ROLE_USER, ROLE_ADMIN). Users are stored in the database.

#### User Management Endpoints
*   `GET /auth/register`
    *   Processes new user registration
*   `POST /auth/register`
    *   Displays the user registration form
    *   username:{username}
    *   password:{password}
*   `GET /auth/login`
    *   Displays the user login form
*   `POST /auth/login`
    *   Processes user authentication
    *   username:{username}
    *   password:{password}
*   `POST /auth/logout`
    *   Logs the current user out

On the first launch, the application creates a default administrator account. Credentials for this account must be configured in the .env file.

## API Endpoints

### Lab2:
#### Book Management
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
*   `PATCH /api/books/{id}/expiration-date`
    *   Change book's expiration date

#### Author Management
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

#### Reader Management
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

#### Fine Management
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

### Lab3:
#### Business Operations
*   `POST /api/library/borrow`
    *   Borrow a Book
    *   {"bookId": 1, "readerId": 1}
*   `POST /api/library/return`
    *   Return a Book
    *   {"bookId": 1}
*   `POST /api/library/pay-fine`
    *   Pay a Fine
    *   {"fineId": 1}  
*   `POST /api/library/check-ban`
    *   Check & Ban Reader
    *   {"readerId": 1}
*   `POST /api/library/process-overdue`
    *   Process Overdue Books
    *   No Body