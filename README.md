# 📊 Zomato Enterprise Data Warehouse (OLAP)

A complete end-to-end Data Warehouse and OLAP (Online Analytical Processing) dashboard built to analyze Zomato's restaurant dataset. This project demonstrates the practical implementation of ETL processes, Star Schema design, and analytical querying.

## 🚀 Tech Stack
* **Backend:** Java, Spring Boot, Spring Data JPA
* **Database:** H2 Database (In-Memory Materialized View) / MySQL
* **Frontend:** Thymeleaf, HTML5, CSS3, Chart.js
* **Data Processing:** OpenCSV

## 🧠 Data Warehouse Concepts Implemented

1. **ETL Pipeline (Extract, Transform, Load):**
    * Automated extraction of raw CSV data using Spring Boot's `CommandLineRunner`.
    * Data cleaning and transformation during the application startup.
    * Bulk loading into the operational database.

2. **Schema Architecture:**
    * Designed around a **Star Schema** concept using conformed dimensions (`dim_restaurant`, `dim_location`) and a centralized fact table (`fact_orders`).
    * Currently optimized as a **Denormalized Materialized View** for faster read performance on the dashboard.

3. **OLAP Operations:**
    * **Roll-up:** Aggregating granular restaurant-level facts into geographical (City) and category-based (Cuisine) metrics.
    * **Slice & Dice:** Filtering the analytical cube based on specific dimensions (e.g., viewing delivery footprints exclusively for "Indore").
    * **Drill-down:** Dynamically transitioning from high-level geographical cost distribution down to specific restaurant-level metrics.

