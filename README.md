Tastory app
A food-ordering web application where customers browse restaurants, view their menus and place orders, while restaurant owners manage their products and track incoming orders. Built with Spring Boot and server-side rendering (Thymeleaf).
This project was developed as the final project for Coding Factory 9 (AUEB).
Features
Authentication & Authorization with Spring Security and three roles: `CUSTOMER`, `RESTAURANT_OWNER`, `ADMIN`.
Customers can register, log in, browse the restaurant list, open a restaurant's menu, place orders and view their own order history with per-order totals.
Restaurant owners can add, edit and delete products, and view / update the status of the orders placed to their restaurants.
Two levels of authorization: URL-based rules in `SecurityConfig`, plus object-level ownership checks inside the service layer (an owner cannot touch another restaurant's products or orders, even by editing the URL).
Order status is modelled as a state machine on the `OrderStatus` enum, so illegal transitions are rejected.
Historical pricing is preserved: each `OrderItem` stores a snapshot of the product price at the time of the order.
Server-side input validation on all forms (Jakarta Bean Validation).
Centralized exception handling with custom exceptions and dedicated 403 / 404 error pages.
Restaurant grid with a live client-side search filter.
Role-aware navigation and route protection (static resources and public pages are open; owner/admin actions are restricted).
Automatic seed data on first startup (admin user, a restaurant owner and 6 sample restaurants with products).
Tech Stack
Layer	Technology
Language	Java 21 (Amazon Corretto)
Framework	Spring Boot 4.1.0 (Spring MVC, Spring Data JPA, Spring Security)
View	Thymeleaf (Server-Side Rendering) + Thymeleaf Extras Spring Security
Persistence	Hibernate / JPA
Database	MySQL 8
Validation	Jakarta Bean Validation (`spring-boot-starter-validation`)
Testing	JUnit 5 + Mockito
Boilerplate	Lombok
Build tool	Maven
Styling	Custom CSS (Poppins font, Font Awesome icons)
Architecture
The application is a layered monolith — a single deployable Spring Boot application whose code is organised in strictly separated layers:
Controllers – handle HTTP requests and return Thymeleaf views.
Service Layer – business logic (order creation, product management, ownership checks).
Repositories – Spring Data JPA interfaces for data access.
Domain Model / Entities – `User`, `Restaurant`, `Product`, `Order`, `OrderItem`, plus the `Role` and `OrderStatus` enums.
DTOs – used for registration and product forms, so entities are never bound directly to web input.
Security config – `SecurityConfig` defines the filter chain, password encoding and authorization rules.
Exception handling – custom exceptions (`NotAuthorizedException`, `ResourceNotFoundException`) handled centrally via `@ControllerAdvice`.
Prerequisites
Make sure the following are installed:
JDK 21 (e.g. Amazon Corretto 21)
Maven 3.9+ (or use the included Maven wrapper `./mvnw`)
MySQL 8 running locally on port 3306
Database Setup
The app connects to a MySQL database named `tastory_db` with a dedicated user. Create them once, for example from the MySQL shell:
```sql
CREATE DATABASE tastory_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'tastory_user'@'localhost' IDENTIFIED BY 'tastory_pass';
GRANT ALL PRIVILEGES ON tastory_db.* TO 'tastory_user'@'localhost';
FLUSH PRIVILEGES;
```
The tables are created automatically by Hibernate on startup (`spring.jpa.hibernate.ddl-auto=update`), so you don't need to write any DDL by hand.
Optional: run MySQL with Docker
Instead of a local MySQL install you can spin one up with Docker:
```bash
docker run --name tastory-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=tastory_db \
  -e MYSQL_USER=tastory_user \
  -e MYSQL_PASSWORD=tastory_pass \
  -p 3306:3306 -d mysql:8
```
Configuration
Connection settings live in `src/main/resources/application.properties`:
```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/tastory_db}
spring.datasource.username=${DB_USERNAME:tastory_user}
spring.datasource.password=${DB_PASSWORD:tastory_pass}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

tastory.admin.email=${ADMIN_EMAIL:admin@tastory.gr}
tastory.admin.password=${ADMIN_PASSWORD:admin123}
```
Credentials are read from environment variables when available, falling back to the development defaults shown above. In a real deployment, set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `ADMIN_EMAIL` and `ADMIN_PASSWORD` as environment variables instead of relying on the committed defaults.
Build & Run
From the project root:
```bash
# 1. Build
mvn clean package        # or: ./mvnw clean package

# 2. Run
mvn spring-boot:run      # or: ./mvnw spring-boot:run
```
Alternatively, run the packaged JAR:
```bash
java -jar target/*.jar
```
You can also open the project in IntelliJ IDEA and run the `TastoryApplication` main class directly.
Once started, the app is available at:
```
http://localhost:8080
```
On the first startup the seed data is inserted automatically (admin, owner and 6 restaurants). On later startups it is skipped.
Tests
Run the test suite with:
```bash
mvn test
```
Unit tests cover the `OrderService` using JUnit 5 and Mockito: ownership checks on status updates, rejection of illegal order-status transitions, and the price-snapshot logic that preserves historical order pricing.
Default Accounts
The seeder creates the following users (passwords are BCrypt-encoded in the database):
Role	Email	Password
Admin	admin@tastory.gr	admin123
Restaurant owner	owner@tastory.gr	owner123
Customers can be created at any time through the Register page (`/register`). All self-registered users are created as `CUSTOMER`; promotion to `RESTAURANT_OWNER` is performed by an admin from the user management page.
Usage
Open `http://localhost:8080` and register as a customer (or log in with the seeded owner account).
Go to Restaurants to see the restaurant grid.
Open a restaurant to view its menu, set quantities and place an order.
Check My orders to see your order history and totals.
Log in as the owner to add/edit products and to change the status of incoming orders from the restaurant's orders page.
Project Structure (indicative)
```
src/main/java/gr/tastory/aueb/
├── TastoryApplication.java
├── config/          # SecurityConfig, DataSeeder
├── model/           # User, Restaurant, Product, Order, OrderItem, Role, OrderStatus
├── repository/      # UserRepo, RestaurantRepo, ProductRepo, OrderRepo, OrderItemRepo
├── service/         # business logic
├── controller/      # MVC controllers
├── exception/       # NotAuthorizedException, ResourceNotFoundException, GlobalExceptionHandler
└── dto/             # RegisterDto, ProductDto

src/test/java/gr/tastory/aueb/
└── service/         # OrderServiceTest

src/main/resources/
├── templates/       # Thymeleaf views (+ fragments/navbar.html, error-403.html, error-404.html)
├── static/css/      # style.css
└── application.properties
```
Author
Developed by Christoforos Nakos for Coding Factory 9 — Athens University of Economics and Business (AUEB).
