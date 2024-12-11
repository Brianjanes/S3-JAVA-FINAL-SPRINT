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

1. Ensure PostgreSQL is running: Before launching the application, ensure the PostgreSQL service is running on your local machine.
2. Database Setup: Follow the steps in the Database Setup section to create and configure your database.
3. Launch the application: Run the EcommApp class. We recommend using IntelliJ for development.

<img width="327" alt="Screenshot 2024-12-11 at 7 10 52 AM" src="https://github.com/user-attachments/assets/06e962db-2d9b-4171-a315-8614906c2e4a">

4. Registration/Login:
Register as a new user or log in with existing credentials.

<p align="left">
  <img src="https://github.com/user-attachments/assets/10f6f41c-dac2-42b6-8347-059dc33c5531" width="517" alt="Registration Screen">
</p>
<p align="left">
  <img src="https://github.com/user-attachments/assets/58323f96-9a2f-4051-af61-afb97c30395b" width="712" alt="Login Screen">
</p>

If you encounter any errors with login or registration (e.g., incorrect credentials), the system will display a corresponding error message.

<p align="left">
  <img src="https://github.com/user-attachments/assets/6c9888ec-9384-4831-add3-dd6139007b0c" width="353" alt="Login Error">
  <img src="https://github.com/user-attachments/assets/20e37cee-f0af-44f4-8c08-53b215a61be7" width="182" alt="Registration Error">
</p>


### Menu Navigation
Navigate the menu based on your role:
   - Buyers can browse and search products.
     <p align="left">
  <img src="https://github.com/user-attachments/assets/183c0751-7330-4ede-aad8-00847b8a1925" width="250" alt="Buyer Menu">
  <img src="https://github.com/user-attachments/assets/2f3589f0-0180-4948-a98b-902d537ad3f7" width="767" alt="Product Browse">
</p>
<p align="left">
  <img src="https://github.com/user-attachments/assets/3a42aea6-0f6b-4116-8bb5-aef3ea2d09a9" width="346" alt="Product Search">
  <img src="https://github.com/user-attachments/assets/0961fd9b-e059-4fa3-aeab-add6f76970ac" width="725" alt="Search Results">
</p>

   - Sellers can manage their product listings.
     <p align="left">
  <img src="https://github.com/user-attachments/assets/b2f2a55e-9526-4ea8-8fb8-bae8033130d4" width="342" alt="Seller Menu">
  <img src="https://github.com/user-attachments/assets/6a94eecc-4e39-4a57-9a7d-60a310e2ff8f" width="831" alt="Product Management">
</p>
<p align="left">
  <img src="https://github.com/user-attachments/assets/e1fd4c05-4f33-4c14-89b4-3525ccaffdde" width="554" alt="Add Product">
  <img src="https://github.com/user-attachments/assets/68cb5209-b6b3-4bda-a298-d821deabeeb1" width="551" alt="Update Product">
  <img src="https://github.com/user-attachments/assets/c1220537-7b82-4853-ba92-4ec855ab718a" width="256" alt="Seller Products">
</p>

   - Admins can manage users and view detailed product listings.
     <p align="left">
  <img src="https://github.com/user-attachments/assets/a74a25e9-2f2d-41cd-a2ac-0df1f2c140a1" width="812" alt="Admin User Management">
  <img src="https://github.com/user-attachments/assets/c5540737-8769-419a-9033-5946812de680" width="552" alt="Delete User">
</p>
<p align="left">
  <img src="https://github.com/user-attachments/assets/d0d28754-d8f7-44e4-b232-e313bab8639b" width="547" alt="View Users">
  <img src="https://github.com/user-attachments/assets/a9cf8919-8647-42f7-a99f-beeef4905a86" width="1312" alt="Product Listings">
</p>
<p align="left">
  <img src="https://github.com/user-attachments/assets/30e035be-d35a-4e91-8453-8adf2c276ee5" width="921" alt="Admin Dashboard">
  <img src="https://github.com/user-attachments/assets/83d01f0d-20c7-42ab-b0e3-9a6c49f5ba1a" width="429" alt="Admin Options">
</p>
---

## Deployment Documentation

### Prerequisites
1. Java 17 or later.
2. PostgreSQL database setup (see Database Setup section).
3. Maven build tool (ensure Maven is installed and configured).

### Build Instructions
1. Ensure Maven is configured: Make sure the pom.xml file is correctly set up in your project. This includes all necessary dependencies for PostgreSQL, BCrypt, and the Lanterna library.
2. Compile the project: Run mvn clean install to compile the project and resolve dependencies. If needed, you can skip tests by using the command mvn clean install -DskipTests, but it's recommended to run tests during development to ensure everything works correctly.
   
### Database Setup
1. Execute the database script: Run the SQL script from the following link to create and populate the necessary database schema: [Database Initialization Script](https://github.com/Brianjanes/S3-JAVA-FINAL-SPRINT/blob/main/JAVA-FINAL-SPRINT/src/main/resources/SQL/modifiedCreate.sql)
2. Check for errors: If you encounter errors like missing tables or connection issues, check the following:
   - Ensure your PostgreSQL service is running.
   - Verify your connection settings (database URL, username, and password).
   - If there are missing tables, double-check the execution of the database script.
3. Update the DatabaseConfig.java file with your local PostgreSQL database credentials:
   ```
  db.url=jdbc:postgresql://localhost:5432/ecommerce_db
  db.username=your_username
  db.password=your_password
   ```
   
### Deployment on IDE
1. Import the project as a Maven project.
2. Ensure dependencies are resolved.
3. Configure a run configuration with `EcommApp` as the main class.

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

### Development Guidelines
- Follow Java naming conventions:
  - PascalCase for classes
  - camelCase for methods and variables
- Use Javadocs for all public classes and methods
- Modularize functionality into logical packages

### Class Diagram

The relationships between the main classes are:

```
classDiagram
    class EcommApp {
        - UserService userService
        - ProductService productService
        - MultiWindowTextGUI gui
        - User currentUser
        - Window currentWindow
        + start()
        + displayMainMenu()
        + handleLogin()
        + handleSignUp()
        + showRoleSpecificMenu()
        + showBuyerMenu()
        + showSellerMenu()
        + listSellerProducts()
        + showAdminMenu()
        + updateUserMenu()
        + displayAllProducts()
        + searchProducts()
        + addProduct()
        + viewAllUsers()
        + viewAllProductsWithSellers()
        + deleteUser()
        + showErrorMessage(String message)
    }
    
    class UserService {
        - UserDAO userDAO
        + login(String username, String password)
        + registerUser(String username, String password, String email, String role)
        + getAllUsers()
        + getUserById(int userId)
        + updateUserField(int userId, String field, String newValue)
        + deleteUser(int userId)
    }
    
    class ProductService {
        + createProduct(String name, String description, double price, int quantity, User currentUser)
        + getSellerProducts(User currentUser)
        + getAllProducts()
        + searchProducts(String keyword)
        + updateProduct(Product product, User currentUser)
        + deleteProduct(int productId, User currentUser)
    }
    
    class UserDAO {
        + getUserByUsername(String username)
        + saveUser(User user)
        + getAllUsers()
        + getUserById(int userId)
        + updateUserField(int userId, String field, String newValue)
        + deleteUser(int userId)
    }
    
    class DatabaseConfig {
        - String URL
        - String USER
        - String PASSWORD
        - Connection connection
        + getConnection()
        + closeConnection()
    }
    
    class DatabaseConnectionTest {
        + main(String[] args)
    }
    
    class Product {
        - int product_id
        - String name
        - String description
        - double price
        - int quantity
        - int seller_id
        + Product(String name, String description, double price, int quantity, int seller_id)
        + Product(int product_id, String name, String description, double price, int quantity, int seller_id)
        + getProduct_id()
        + getName()
        + getDescription()
        + getPrice()
        + getQuantity()
        + getSeller_id()
        + setProduct_id(int product_id)
        + setName(String name)
        + setDescription(String description)
        + setPrice(double price)
        + setQuantity(int quantity)
        + setSeller_id(int seller_id)
    }
    
    EcommApp "1" --o "1" UserService
    EcommApp "1" --o "1" ProductService
    UserService "1" --o "1" UserDAO
    ProductService "1" --o "*" Product
    DatabaseConfig "1" --o "1" DatabaseConnectionTest```

---

## Additional Notes
- Video overview of the project:
  

https://github.com/user-attachments/assets/ec96fb9a-db49-4c39-8742-808c3e0f7507


  
- For any issues or contributions, please contact the development team.

---

## Contributors
- [**Kyle Hollett**](https://github.com/kyhol)
- [**Brad Ayers**](https://github.com/BradTheeStallion)
- [**Brian Janes**](https://github.com/Brianjanes)
