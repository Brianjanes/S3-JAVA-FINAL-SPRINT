# S3-JAVA-FINAL-SPRINT

# E-Commerce Platform

## Project Overview

Welcome to our Final Sprint project for Advanced Programming and Java at Keyin College in St. John's, NL, Canada. This console-based E-Commerce platform is designed to simulate an online marketplace where users can register as buyers, sellers, or admins. The platform provides functionality for browsing products, managing listings, and performing administrative tasks. We chose to give our project a retro twist by going with a simple old-school GUI instead of a standard console menu.

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
1. Launch the application by running the main class `EcommApp`. We recommend using IntelliJ.
2. Register as a new user or log in using existing credentials.

### Menu Navigation
Navigate the menu based on your role:
   - Buyers can browse and search products.
   - Sellers can manage their product listings.
   - Admins can manage users and view detailed product listings.

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

### Directory Structure

<img width="300" alt="Screenshot 2024-12-11 at 12 23 35 PM" src="https://github.com/user-attachments/assets/7d848073-94a9-4d6f-b49d-8f078d62ae64">

### Dependencies
The project uses the following dependencies:
- [Lanterna](https://github.com/mabe02/lanterna) for the console UI.
- [BCrypt](https://www.mindrot.org/projects/jBCrypt/) for password encryption.
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/) for database connectivity.
- [JUnit](https://junit.org/) for unit testing.

### Build Instructions
1. Ensure Maven is installed and configured.
2. Navigate to the project directory.
3. Run `mvn clean install` or `mvn clean install -DskipTests` to compile the project and resolve dependencies.

### Development Guidelines
- Follow Java naming conventions:
  - PascalCase for classes
  - camelCase for methods and variables
- Use Javadocs for all public classes and methods
- Modularize functionality into logical packages

### Testing
Unit tests are implemented using JUnit. To run tests:
1. Execute `mvn test` in the terminal.
2. Review the test results for functionality verification.

### Documentation
To generate project Javadocs:
1. Run `mvn javadoc:javadoc`
2. Open the generated documentation at `target/site/apidocs/index.html`

---

## Deployment Documentation

### Prerequisites
1. Java 17 or later.
2. PostgreSQL database setup.
3. Maven build tool.

### Database Setup
1. Execute the following SQL script to create and populate the database:

[Database Initialization Script](https://github.com/Brianjanes/S3-JAVA-FINAL-SPRINT/blob/main/JAVA-FINAL-SPRINT/src/main/resources/SQL/modifiedCreate.sql)

### Running the Application
1. Ensure the PostgreSQL server is running and accessible.
2. Update database connection settings in DatabaseConfig.java:
   ```
   db.url=jdbc:postgresql://localhost:5432/ecommerce_db
   db.username=your_username
   db.password=your_password
   ```
3. Execute `java -jar target/EcommApp.jar` to start the application.

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
