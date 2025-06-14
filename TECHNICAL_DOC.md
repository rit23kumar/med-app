## MedApp: Technical Documentation

This document provides a technical overview of the MedApp backend application, including its architecture, database schema, and REST API endpoints.

---

### 1. Introduction

MedApp is a Spring Boot application designed to manage medicine inventory, sales, and purchase history. It provides a RESTful API for frontend applications to interact with its core functionalities.

---

### 2. Architecture Overview

MedApp follows a layered architecture, common in Spring Boot applications:
*   **Controller Layer:** Handles incoming HTTP requests and routes them to appropriate service methods.
*   **Service Layer:** Contains business logic and orchestrates operations between controllers and repositories.
*   **Repository Layer:** Interacts with the database, performing CRUD operations using Spring Data JPA.
*   **Entity Layer:** Defines the database table structures.
*   **DTO Layer:** Data Transfer Objects used for data exchange between layers and API.

---

### 3. Database Schema

The application uses a relational database, with entities mapped using JPA.

#### 3.1. `Medicine` Table

Represents individual medicine types.
*   **Table Name:** `Medicine`
*   **Fields:**
    *   `id` (Long): Primary Key, auto-generated.
    *   `name` (String): Unique name of the medicine.

#### 3.2. `Med_Stock` Table

Represents batches of medicine stock.
*   **Table Name:** `Med_Stock`
*   **Fields:**
    *   `id` (Long): Primary Key, auto-generated.
    *   `Med_ID` (Long): Foreign Key to `Medicine` table (medicine ID).
    *   `expDate` (LocalDate): Expiration date of the stock batch.
    *   `quantity` (Integer): Total quantity of medicine in this batch.
    *   `availableQuantity` (Integer): Current available quantity of medicine in this batch.
    *   `price` (Double): Unit price of the medicine in this batch.
    *   `createdAt` (LocalDateTime): Timestamp when this stock entry was created (automatically managed by `@CreationTimestamp`).

#### 3.3. `Sell` Table

Represents a sales transaction.
*   **Table Name:** `Sell`
*   **Fields:**
    *   `id` (Long): Primary Key, auto-generated.
    *   `date` (LocalDateTime): Date and time of the sale.
    *   `Total_Amount` (Double): Total amount of the sale (calculated from `SellItem`s).
    *   `customer` (String, nullable): Name of the customer (optional).
    *   `mode_of_payment` (String): Payment method (e.g., "Cash", "UPI", "Card").

#### 3.4. `Sell_Item` Table

Represents individual items within a sales transaction.
*   **Table Name:** `Sell_Item`
*   **Fields:**
    *   `id` (Long): Primary Key, auto-generated.
    *   `Sell_ID` (Long): Foreign Key to `Sell` table (sale ID).
    *   `Med_ID` (Long): Foreign Key to `Medicine` table (medicine ID).
    *   `Exp_date` (LocalDate): Expiry date of the sold medicine batch.
    *   `quantity` (Integer): Quantity of this medicine item sold.
    *   `price` (Double): Unit price at which this medicine item was sold.
    *   `discount` (Double): Discount percentage applied to this item.

---

### 4. REST API Endpoints

All API endpoints are prefixed with `/api`.

#### 4.1. Medicine Endpoints (`/api/medicines`)

*   **`POST /api/medicines`**
    *   **Description:** Adds a new medicine.
    *   **Request Body:** `MedicineDto` (name).
    *   **Response:** `MedicineDto` of the added medicine.
*   **`POST /api/medicines/batch`**
    *   **Description:** Adds multiple medicines in a batch.
    *   **Request Body:** `List<MedicineDto>` (list of medicine names).
    *   **Response:** `BatchMedicineResponse` (includes successful and failed medicines).
*   **`POST /api/medicines/with-stock`**
    *   **Description:** Adds a new medicine with initial stock or updates stock for an existing medicine.
    *   **Request Body:** `MedicineWithStockDto` (contains `MedicineDto` and `StockDto`).
    *   **Response:** `MedicineWithStockDto`.
*   **`GET /api/medicines`**
    *   **Description:** Retrieves all medicines with pagination.
    *   **Query Parameters:** `page` (int, default 0), `size` (int, default 10).
    *   **Response:** `Page<Medicine>`.
*   **`GET /api/medicines/{id}`**
    *   **Description:** Retrieves a medicine by its ID.
    *   **Path Variable:** `id` (Long).
    *   **Response:** `MedicineDto` or 404 Not Found.
*   **`GET /api/medicines/search`**
    *   **Description:** Searches medicines by name.
    *   **Query Parameters:**
        *   `name` (String): Search term.
        *   `searchType` (String, default "contains"): "contains" or "startsWith".
    *   **Response:** `List<Medicine>`.
*   **`GET /api/medicines/{id}/stock-history`**
    *   **Description:** Retrieves stock history for a specific medicine.
    *   **Path Variable:** `id` (Long).
    *   **Query Parameters:** `includeFinished` (boolean, default false): If true, includes stock with 0 available quantity.
    *   **Response:** `List<StockHistoryResponse>`.
*   **`GET /api/medicines/all`**
    *   **Description:** Retrieves all medicines without pagination, sorted by name ascending.
    *   **Response:** `List<Medicine>`.

#### 4.2. History Endpoints (`/api/history`)

*   **`POST /api/history/sales`**
    *   **Description:** Retrieves sales history for a given date range.
    *   **Request Body:** `DateRangeRequest` (contains `fromDate` and `toDate` as LocalDate).
    *   **Response:** `List<Sell>`.
*   **`POST /api/history/purchases`**
    *   **Description:** Retrieves purchase history for a given date range.
    *   **Request Body:** `DateRangeRequest` (contains `fromDate` and `toDate` as LocalDate).
    *   **Response:** `List<MedStock>`.

#### 4.3. Sells Endpoints (`/api/sells`)

*   **`POST /api/sells`**
    *   **Description:** Creates a new sales transaction.
    *   **Request Body:** `CreateSellRequest` (customer, modeOfPayment, list of sell items).
    *   **Response:** `Sell`.
*   **`GET /api/sells`**
    *   **Description:** Retrieves all sales with pagination.
    *   **Query Parameters:** `page` (int, default 0), `size` (int, default 10).
    *   **Response:** `Page<Sell>`.
*   **`GET /api/sells/{id}`**
    *   **Description:** Retrieves a sales transaction by its ID.
    *   **Path Variable:** `id` (Long).
    *   **Response:** `Sell` or 404 Not Found.

---

### 5. Other Technical Details

*   **Spring Boot:** The application is built using Spring Boot, providing a quick way to create stand-alone, production-grade Spring applications.
*   **Spring Data JPA:** Used for database interaction, simplifying data access with repository interfaces.
*   **Lombok:** Used to reduce boilerplate code (e.g., getters, setters, constructors) with annotations like `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.
*   **Timezone Handling:**
    *   For `Sell` entities, the `date` field is explicitly set to `LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Kolkata"))` to ensure consistency.
    *   For `Med_Stock` entities, the `createdAt` field uses `@CreationTimestamp`, which relies on the JVM's default timezone. The `application.properties` sets `spring.jpa.properties.hibernate.jdbc.time_zone=Asia/Kolkata` to align this with the desired timezone for JDBC operations.
*   **Error Handling:** Custom exceptions and `ResponseEntity` are used to provide meaningful error responses to the client.
*   **Cross-Origin Resource Sharing (CORS):** Configured to allow requests from any origin (`@CrossOrigin(origins = "*")`) for flexible frontend integration.

---

This document provides a foundational understanding of MedApp's backend. For more detailed insights, refer to the source code.

--- 