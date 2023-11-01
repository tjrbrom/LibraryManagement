# Library Management System

The Library Management System is a Vert.x-based application that allows people
to manage books and borrowers in a library. This README provides an overview of
the project, its features, and how to get started.

## Features

- **Books Management:** Add, retrieve, and delete books from the library.
- **Borrowers Management:** Add, retrieve, and delete information about people
  borrowing books.
- **Database Integration:** Data is stored in a MySQL database, and the system
  provides CRUD (Create, Read, Update, Delete) operations.

## API Endpoints

- **Get Book by ISBN**
    - URL: `/api/libmanagement/books/book/{isbn}`
    - Method: GET
    - Description: Retrieve book information by ISBN.
    - Response: JSON representation of the book or HTTP 404 if not found.

- **Get Person by ID**
    - URL: `/api/libmanagement/people/person/{id}`
    - Method: GET
    - Description: Retrieve person information by ID.
    - Response: JSON representation of the person or HTTP 404 if not found.

## Prerequisites

Before getting started, ensure you have the following:

- Java 11 or later
- Maven (for building the project)
- Docker (optional, for containerization)

## Getting Started

Follow these steps to get the Library Management System up and running.

### Clone and build the application

   ```
   git clone https://github.com/your-username/library-management.git
   cd library-management
   mvn clean install -DskipTests
   ```

### Running the Application

#### Using Docker (Recommended)

   ```
   docker build -t library-management-app .
   docker run -p 8080:8080 library-management-app
   ```

## License

This project is licensed under the [MIT License](LICENSE).