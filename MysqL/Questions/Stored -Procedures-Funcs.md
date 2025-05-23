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


```
::contentReference[oaicite:105]{index=105}
 
```

```sql
#
🧾
SQL Stored Procedures & Functions: Interview Questions

##
🔹 Stored Procedures

1. **What is a stored procedure, and how does it differ from a function in SQL?**

2. **Explain the advantages of using stored procedures in database management.**

3. **Can you describe the different types of stored procedures in SQL Server?**

4. **Write a stored procedure that accepts an employee ID and returns the employee's details.**

5. **How would you handle error handling within a stored procedure?**

6. **What are the different parameter modes available in stored procedures?**

7. **Explain the concept of nested stored procedures.**

8. **How would you optimize a stored procedure that performs poorly?**

9. **What is the purpose of the `RETURN` statement in a stored procedure?**

10. **Can a stored procedure return multiple result sets? If so, how?**

## 🔹 Functions

1. **What is a function in SQL, and how is it different from a stored procedure?**

2. **Explain the types of functions available in SQL Server.**

3. **Write a scalar function that calculates the area of a circle given its radius.**

4. **What is a table-valued function, and how does it differ from a scalar function?**

5. **Can a function modify database state? Explain why or why not.**

6. **How would you handle error handling within a function?**

7. **What are the limitations of using functions in SQL queries?**

8. **Explain the concept of deterministic and nondeterministic functions.**

9. **How can you use a function within a `SELECT` statement? Provide an example.**

10. **What is the purpose of the `RETURNS` clause in a function definition?**

## 🔹 Advanced Topics

1. **Discuss the performance implications of using stored procedures and functions.**

2. **How would you implement version control for stored procedures and functions?**

3. **Explain the concept of CLR (Common Language Runtime) integration in SQL Server.**

4. **What are the security considerations when using stored procedures and functions?**

5. **How can you debug a stored procedure or function in SQL Server?**

6. **Discuss the role of stored procedures and functions in database normalization.**

7. **Explain the concept of recursion in stored procedures and functions.**

8. **How would you handle transactions within a stored procedure?**

9. **What is the difference between a system-defined and a user-defined function?**

10. **Discuss the use of stored procedures and functions in implementing business logic.**

```