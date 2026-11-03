# Java Car Fleet Management Application (JDBC, H2 Database & DAO Pattern)

A multi-layered, console-based Java application built to manage a complete vehicle rental ecosystem. This project uses an embedded H2 relational database managed via pure JDBC, featuring a clean separation of concerns through the Data Access Object pattern, robust error handling, and database-level state management.

This was built as my first deep dive into relational databases, JDBC, and structured application architecture.

---

## 🚀 Key Features

* **Manager & Customer Portals:** Role-based access supporting company creation, car fleet management, customer registration, and vehicle rentals.
* **Dynamic Inventory Filtering:** Utilizes SQL subqueries with `NOT IN` clauses to automatically hide rented cars from available inventories and return them to the pool upon return.
* **Data Integrity & Constraints:** Relies on RDBMS foreign keys, unique constraints, and transaction management to enforce business rules directly at the database level.
* **Graceful Exception Handling:** Explicitly catches SQL integrity violations (such as duplicate company or customer names) to provide clean console messages instead of runtime crashes.

---

## 🏛️ Architecture & Design Choices

To keep the codebase modular and maintainable, the application is divided into three distinct layers:

1. **Presentation / CLI Layer (`Main`, `UserInput`, `Printer`, `*Actions`):** Manages console loops, menu routing, and user inputs using custom validators.
2. **Data Access Layer (`*DAO` / `*DAOImpl`):** Abstracts SQL operations away from business logic, ensuring clean interfaces for interacting with data.
3. **Persistence Layer (`DbClient`):** A thread-safe Singleton client that handles the H2 database lifecycle, connection strings, and statement execution.

---

## 🛠️ Engineering Polish & Edge-Case Handling

* **Configurable Database Startup:** Supports command-line argument parsing (`DbConfig`) to dynamically specify custom database filenames (e.g., `-databaseFileName custom_name`).
* **Robust CLI Input Validation:** Employs a centralized `Validator` class to catch non-numeric entries (`NumberFormatException`) and out-of-bounds menu selections before they cause runtime crashes.
* **Graceful Exception Management:** Explicitly intercepts H2 integrity constraint violations (`JdbcSQLIntegrityConstraintViolationException`) to display clean error messages (like `"Company with this name already exists.\n"`) rather than exposing raw stack traces.

---

## 🗄️ Database Schema

The application runs on an embedded H2 database in MySQL compatibility mode (`MODE=MySQL`) and enforces relational integrity across three tables:

```sql
-- COMPANY Table
CREATE TABLE IF NOT EXISTS COMPANY (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR UNIQUE NOT NULL
);

-- CAR Table (Linked to Company)
CREATE TABLE IF NOT EXISTS CAR (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR UNIQUE NOT NULL,
    COMPANY_ID INT NOT NULL,
    CONSTRAINT fk_company_id FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)
);

-- CUSTOMER Table (Tracks Active Rentals)
CREATE TABLE IF NOT EXISTS CUSTOMER (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR UNIQUE NOT NULL,
    RENTED_CAR_ID INT DEFAULT NULL,
    CONSTRAINT fk_car_id FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)
);
```

### The Core Challenge: Dynamic Subqueries
Instead of writing complex, multi-step Java loops to check car availability, I handled state filtering directly inside the database engine. When a customer views a company's available cars, the system executes a single query featuring a subquery:

``` SQL
private final String SELECT_ALL_AVAILABLE_CARS_PER_COMPANY = 
    "SELECT * FROM CAR " +
    "WHERE company_id = ? AND " +
    "id NOT IN (SELECT rented_car_id FROM CUSTOMER WHERE rented_car_id IS NOT NULL)";
```

## 💻 Example Walkthrough / Usage
When you launch the application, you are greeted with the main login menu:

```
Connecting to database...
1. Log in as a manager
2. Log in as a customer
3. Create a customer
0. Exit
> 1
```
### Manager Flow:
1. View/Create Companies: Managers can view existing companies or add new ones.

2. Manage Fleet: Selecting a company allows the manager to view cars registered to that company or add new vehicles to the fleet.

```
1. Company list
2. Create a company
0. Back
> 1

Choose the company:
1. Alpha Rentals
2. Beta Motors
0. Back
> 1

'Alpha Rentals' company
1. Car list
2. Create a car
0. Back
```

### Customer Flow:
Customers can log in using their ID, choose a company, and rent an available car. The system prevents double-booking if the customer already has an active rental.

```
Your rented car:
Tesla Model 3
Company:
Alpha Rentals
```

## 🛠️ Tech Stack
* **Language:** Java
* **Database:** H2 Database Engine (Embedded, JDBC)
* **Design Patterns:** Singleton, DAO (Data Access Object)
