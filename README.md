# S3-JAVA-FINAL-SPRINT

# E-Commerce Platform

## Project Overview

Welcome to our Final Sprint project for Advanced Programming and Java at Keyin College in St. John's, NL, Canada. This console-based E-Commerce platform is designed to simulate an online marketplace where users can register as buyers, sellers, or admins. The platform provides functionality for browsing products, managing listings, and performing administrative tasks.

---

## Features

### General Features
- User registration and login with role-based access control.
- Secure password storage using BCrypt.
- PostgreSQL integration for persistent data storage.
- Console-based user interface built with the Lanterna library.

### Role-Specific Features
- **Buyers:**
  - Browse all products.
  - Search for specific products.

- **Sellers:**
  - Add new products.
  - View, update, and delete their products.

- **Admins:**
  - View all users and their contact details.
  - Delete users.
  - View all products along with seller details.

---

## User Documentation

### Getting Started
1. Launch the application by running the main class `EcommApp`.
2. Register as a new user or log in using existing credentials.
3. Navigate the menu based on your role:
   - Buyers can browse and search products.
   - Sellers can manage their product listings.
   - Admins can manage users and view detailed product listings.

### Menu Navigation
Each menu displays clear options:
- Use numeric inputs to select options.
- Follow on-screen prompts for additional input when required.

### Class Diagram
The relationships between the main classes are:

```
User <|-- Buyer
User <|-- Seller
User <|-- Admin

UserDAO <>---- User
UserService <>---- UserDAO
Product <>---- User (via seller_id)
ProductDAO <>---- Product
ProductService <>---- ProductDAO
```

---

## Development Documentation

### Code Structure

```plaintext
src/
|-- com.keyin/
    |-- User/
    |-- Roles/
    |-- Products/
    |-- EcommApp.java
```
- **User:** Contains user-related classes, DAOs, and services.
- **Roles:** Implements role-specific functionalities.
- **Products:** Handles product-related operations.

### Dependencies
The project uses the following dependencies:
- [Lanterna](https://github.com/mabe02/lanterna) for the console UI.
- [BCrypt](https://www.mindrot.org/projects/jBCrypt/) for password encryption.
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/) for database connectivity.

### Build Instructions
1. Ensure Maven is installed and configured.
2. Navigate to the project directory.
3. Run `mvn clean install` to compile the project and resolve dependencies.

### Testing
Unit tests are implemented using JUnit. To run tests:
1. Execute `mvn test` in the terminal.
2. Review the test results for functionality verification.

---

## Deployment Documentation

### Prerequisites
1. Java 17 or later.
2. PostgreSQL database setup.
3. Maven build tool.

### Database Setup
1. Execute the following SQL script to create and populate the database:

https://github.com/Brianjanes/S3-JAVA-FINAL-SPRINT/blob/main/JAVA-FINAL-SPRINT/src/main/resources/SQL/modifiedCreate.sql

### Running the Application
1. Ensure the PostgreSQL server is running and accessible.
2. Update database connection settings in DatabaseConfig.java:
   ```
   db.url=jdbc:postgresql://localhost:5432/ecommerce_db
   db.username=your_username
   db.password=your_password
   ```
6. Execute `java -jar target/EcommApp.jar` to start the application.

### Deployment on Another IDE
1. Import the project as a Maven project.
2. Ensure dependencies are resolved.
3. Configure a run configuration with `EcommApp` as the main class.

---

## Additional Notes
- Comprehensive error handling is implemented throughout the application.
- For any issues or contributions, please contact the development team.

---

## Contributors
- **Kyle Hollett**
- **Brad Ayers**
- **Brian Janes**



