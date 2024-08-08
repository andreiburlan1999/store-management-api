Store Management System

Description

The Store Management System is a web application designed for managing products, categories, users, and orders in a store. The application allows for adding, updating, and viewing products and categories, as well as managing customer orders. Users can have different roles (ADMIN, CUSTOMER) with corresponding permissions.

Requirements
JDK 22 or later
Maven 3.8.6 or later
Spring Boot 3.3.2 or later
H2 Database (for development) or another SQL database

Installation

Clone the repository:
git clone https://github.com/username/store-management-system.git
cd store-management-system

Build and run the application:
Ensure you have Maven installed and configured. Use the following command to build and run the application:
mvn clean install
mvn spring-boot:run

Alternatively, you can build a JAR and run it:
mvn clean package
java -jar target/store-management-system-0.0.1-SNAPSHOT.jar

Access the application:
Once the application is running, you can access it at http://localhost:8080.

API Endpoints

Category
GET /api/categories - Retrieve all categories
GET /api/categories/{id} - Retrieve a category by ID
POST /api/categories - Create a new category (ADMIN)
PUT /api/categories/{id} - Update an existing category (ADMIN)

Product
GET /api/products - Retrieve all products
GET /api/products/{id} - Retrieve a product by ID
POST /api/products - Create a new product (ADMIN)
PUT /api/products/{id} - Update an existing product (ADMIN)
DELETE /api/products/{id} - Disable a product (ADMIN)
GET /api/products/category/{categoryId} - Retrieve products by category

User
GET /api/users - Retrieve all users (ADMIN)
GET /api/users/{id} - Retrieve a user by ID (ADMIN)
POST /api/users - Create a new user (ADMIN)
PUT /api/users/{id} - Update an existing user (ADMIN)
GET /api/users/current - Retrieve the current user (ADMIN, CUSTOMER)

Order
GET /api/orders - Retrieve all orders (ADMIN)
GET /api/orders/{id} - Retrieve an order by ID (ADMIN)
POST /api/orders - Create a new order (ADMIN, CUSTOMER)
GET /api/orders/current - Retrieve orders for the current user (ADMIN, CUSTOMER)

Authorization and Authentication
The application uses role-based security to control access to different endpoints:
ADMIN: Full access to all endpoints
CUSTOMER: Limited access, only to endpoints necessary for managing orders and viewing products and categories