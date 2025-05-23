````md
# 🧾 SQL Stored Procedures & Functions: Quick Reference

## 📌 Stored Procedures

### What is a Stored Procedure?

A **stored procedure** is a precompiled collection of SQL statements stored in the database. It can accept input parameters, perform operations, and return results or status codes. Stored procedures help centralize business logic, reduce redundancy, and enhance performance.:contentReference[oaicite:6]{index=6}

### Syntax


```sql
CREATE PROCEDURE procedure_name
    @param1 datatype,
    @param2 datatype OUTPUT
AS
BEGIN
    -- SQL statements
END;
````



### Key Features

* **Reusability**: Execute the same logic multiple times with different parameters.
* **Performance**: Precompiled execution plans improve speed.
* **Security**: Limit direct access to underlying tables.
* **Maintainability**: Centralize logic for easier updates.
* **Transaction Control**: Manage transactions within the procedure.([GeeksforGeeks][1], [TutorialsPoint][2])

### Example

```sql
CREATE PROCEDURE GetEmployeeDetails
    @EmployeeID INT
AS
BEGIN
    SELECT * FROM Employees WHERE EmployeeID = @EmployeeID;
END;
```



### Use Cases

* Automating repetitive tasks.
* Implementing complex business logic.
* Validating and processing data.
* Managing transactions.
* Enhancing security by encapsulating SQL code.([GeeksforGeeks][1])


---

## 🔧 Functions in SQL

### What is a Function?

A **function** is a database object that performs a specific task, returns a value, and can be used within SQL expressions. Functions can be built-in or user-defined.([W3Schools][3], [GeeksforGeeks][4])

### Types of Functions

* **Scalar Functions**: Return a single value (e.g., `LEN()`, `UPPER()`).
* **Aggregate Functions**: Operate on a set of values and return a single value (e.g., `SUM()`, `AVG()`).
* **Table-Valued Functions**: Return a table (e.g., inline and multi-statement functions).

### Syntax

```sql
CREATE FUNCTION function_name (@param datatype)
RETURNS return_datatype
AS
BEGIN
    -- Function logic
    RETURN value;
END;
```



### Example

```sql
CREATE FUNCTION dbo.CelsiusToFahrenheit (@Celsius FLOAT)
RETURNS FLOAT
AS
BEGIN
    RETURN (@Celsius * 1.8) + 32;
END;
```



### Use Cases

* Performing calculations within queries.
* Data transformation and formatting.
* Encapsulating reusable logic.
* Enhancing query readability and maintainability.([en.wikipedia.org][5])


---

## ✅ Comparison: Stored Procedures vs. Functions

| Feature          | Stored Procedure                           | Function                             |                                                                                                                                                                        |
| ---------------- | ------------------------------------------ | ------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Return Value     | Optional (can return status or result set) | Must return a value                  |                                                                                                                                                                        |
| Usage in Queries | Cannot be used in SELECT statements        | Can be used in SELECT, WHERE, etc.   |                                                                                                                                                                        |
| Side Effects     | Can modify database state                  | Cannot modify database state         |                                                                                                                                                                        |
| Parameters       | Input, Output, Input/Output                | Input only                           |                                                                                                                                                                        |
| Control-of-Flow  | Supports complex logic (e.g., loops, if)   | Limited control-of-flow capabilities | ([Department of Computer Science][6], [en.wikipedia.org][7], [en.wikipedia.org][5], [GeeksforGeeks][1], [en.wikipedia.org][8], [Programiz][9], [en.wikipedia.org][10]) |

---

## 📝 Interview-Style Questions

### Basic Understanding

1. **What is a stored procedure, and how does it differ from a function in SQL?**
2. **Explain the purpose of parameters in stored procedures.**
3. **What are the advantages of using functions in SQL queries?**

### Practical Implementation

4. **Write a stored procedure that updates the salary of an employee based on their ID.**
5. **Create a function that calculates the age of a person given their birthdate.**
6. **How would you handle error handling within a stored procedure?**

### Advanced Topics

7. **Discuss the implications of using functions in SELECT statements.**
8. **Explain how stored procedures can enhance database security.**
9. **What are the performance considerations when using stored procedures and functions?**

---

## 📚 Further Reading

* [SQL Stored Procedures - W3Schools](https://www.w3schools.com/sql/sql_stored_procedures.asp)
* [SQL Server Functions - W3Schools](https://www.w3schools.com/sql/sql_ref_sqlserver.asp)
* [SQL Functions (Aggregate and Scalar Functions) - GeeksforGeeks](https://www.geeksforgeeks.org/sql-functions-aggregate-scalar-functions/)
* [Stored Procedures: A Practical Example - Medium](https://medium.com/@mattdamberg/stored-procedures-a-practical-example-e1e46b65050a)

```
::contentReference[oaicite:105]{index=105}
 
```

