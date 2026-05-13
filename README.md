# Irish Tax Calculator (2025)

A complete, object-oriented Java application that calculates Irish personal taxes (Income Tax, Universal Social Charge, and PRSI) based on the official Revenue.ie rules for the 2025 tax year.

## 📌 Overview

This project was built to demonstrate solid Object-Oriented Programming (OOP) principles, clean architecture, and test-driven development (TDD) in Java. It takes a complex real-world domain—the Irish tax system—and models it using clean, maintainable code.

### Features
* **Income Tax Calculation:** Supports standard (20%) and higher (40%) rate bands.
* **Filing Statuses:** Handles Single, Married (one and two incomes), Single Parent, and Widowed statuses, automatically adjusting rate bands and tax credits.
* **Universal Social Charge (USC):** Calculates all four standard USC bands, plus the exemption threshold and reduced rates for those over 70.
* **PRSI:** Calculates Class A (PAYE employees) and Class S (Self-employed/Directors) contributions, including the minimum contribution rules.
* **Interactive CLI:** A clean, terminal-based interface to input data and view a detailed tax breakdown.

## 🏗️ Architecture & OOP Concepts

The application is structured into clear, distinct layers:

* **`model`**: Immutable data structures (`TaxProfile`, `TaxResult`) and Enums (`FilingStatus`, `EmploymentType`) representing the domain.
* **`rules`**: Centralized, final classes (`TaxBands2025`) containing the statutory tax rates and thresholds. This makes annual updates trivial.
* **`service`**: The core business logic. Divided into focused calculators (`IncomeTaxCalculator`, `UscCalculator`, `PrsiCalculator`) adhering to the Single Responsibility Principle (SRP). These are orchestrated by the `TaxCalculatorService` (Facade Pattern).
* **`cli`**: The presentation layer, keeping I/O completely separate from business logic.

## 🚀 Getting Started

### Prerequisites
* Java 17 or higher
* Maven 3.6+

### Build and Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/tiagodof/java_Simplifier.git
   cd java_Simplifier
   ```

2. **Compile and run tests:**
   ```bash
   mvn clean test
   ```

3. **Package the application:**
   ```bash
   mvn clean package
   ```

4. **Run the interactive calculator:**
   ```bash
   java -jar target/irish-tax-calculator-1.0.0.jar
   ```

## 🧪 Testing

The project includes a comprehensive suite of unit and integration tests using **JUnit 5**.
Tests cover edge cases such as:
* Income crossing multiple tax bands.
* Spousal income increasing the standard rate cut-off point.
* USC exemptions and age-related reduced rates.
* PRSI minimum contributions for self-employed individuals.

Run the tests using:
```bash
mvn test
```

## 📄 License

This project is licensed under the MIT License.
