

## 🔗 **SQL JOINs: A Concise Guide**

SQL JOINs allow you to combine rows from two or more tables based on related columns. Understanding the different types of JOINs is crucial for effective data retrieval.

### 1. **INNER JOIN**

* **Definition**: Returns records that have matching values in both tables.
* **Use Case**: When you need only the records with matches in both tables.
* **Example**:

  ```sql
  SELECT users.name, orders.order_id
  FROM users
  INNER JOIN orders ON users.id = orders.user_id;
  ```

### 2. **LEFT (OUTER) JOIN**

* **Definition**: Returns all records from the left table, and the matched records from the right table. If there's no match, NULLs are returned for right table's columns.
* **Use Case**: When you need all records from the left table, regardless of matches in the right table.
* **Example**:

  ```sql
  SELECT users.name, orders.order_id
  FROM users
  LEFT JOIN orders ON users.id = orders.user_id;
  ```

### 3. **RIGHT (OUTER) JOIN**

* **Definition**: Returns all records from the right table, and the matched records from the left table. If there's no match, NULLs are returned for left table's columns.
* **Use Case**: When you need all records from the right table, regardless of matches in the left table.
* **Example**:

  ```sql
  SELECT users.name, orders.order_id
  FROM users
  RIGHT JOIN orders ON users.id = orders.user_id;
  ```

### 4. **FULL (OUTER) JOIN**

* **Definition**: Returns all records when there is a match in either left or right table. If there's no match, NULLs are returned for missing matches in either table.
* **Use Case**: When you need all records from both tables, with NULLs where there is no match.
* **Note**: Not all SQL databases support FULL OUTER JOIN directly.
* **Example**:

  ```sql
  SELECT users.name, orders.order_id
  FROM users
  FULL OUTER JOIN orders ON users.id = orders.user_id;
  ```

### 5. **CROSS JOIN**

* **Definition**: Returns the Cartesian product of the two tables, i.e., all possible combinations of rows.
* **Use Case**: When you need to combine every row of the first table with every row of the second table.
* **Example**:

  ```sql
  SELECT users.name, products.product_name
  FROM users
  CROSS JOIN products;
  ```

### 6. **SELF JOIN**

* **Definition**: A regular join but the table is joined with itself.
* **Use Case**: When you need to compare rows within the same table.
* **Example**:

  ```sql
  SELECT a.name AS Employee1, b.name AS Employee2
  FROM employees a
  JOIN employees b ON a.manager_id = b.id;
  ```

### 7. **NATURAL JOIN**

* **Definition**: Automatically joins tables based on all columns with the same name and data type in both tables.
* **Use Case**: When tables have one or more columns with the same names and you want to join on all of them.
* **Caution**: Be careful with NATURAL JOINs as they can lead to unexpected results if not used properly.

---


```sql
# SQL JOIN Practice Questions (Intermediate Level)

---

## 🔹 INNER JOIN

1. **List all posts along with the usernames of their authors.**

2. **Retrieve all comments with the corresponding post titles and commenter usernames.**

3. **Find all orders along with the names of the users who placed them.**

4. **Get all products that have been ordered, including the order ID and quantity ordered.**

---

## 🔹 LEFT JOIN

5. **List all users and their posts, including users who haven't written any posts.**

6. **Retrieve all products along with their categories, including products that don't belong to any category.**

7. **Show all posts and their associated tags, including posts without any tags.**

---

## 🔹 RIGHT JOIN

8. **List all categories and the posts associated with them, including categories that don't have any posts.**

9. **Retrieve all tags and the posts they're associated with, including tags not linked to any posts.**

---

## 🔹 FULL OUTER JOIN (Simulated using UNION)

10. **List all users and their orders, including users without orders and orders without associated users.**

    *Note: Since MySQL doesn't support FULL OUTER JOIN directly, use UNION of LEFT and RIGHT JOINs.*

---

## 🔹 CROSS JOIN

11. **Generate a list of all possible combinations of users and products (e.g., for a recommendation system).**

12. **Create a list of all possible pairs of categories and tags.**

---

## 🔹 SELF JOIN

13. **Find all users who share the same city in their profiles.**

14. **List all posts that have the same title as another post (duplicate titles).**

---

## 🔹 JOIN with Aggregate Functions

15. **Find the number of posts each user has written.**

16. **Retrieve the total quantity of each product ordered.**

17. **List users who have placed more than 5 orders.**

---

## 🔹 JOIN with Subqueries

18. **Find users who have written posts in more than one category.**

19. **Retrieve products that have never been ordered.**

20. **List posts that have more comments than the average number of comments per post.**

---

## 🔹 JOIN with Filtering

21. **List all orders placed in the last 30 days along with user information.**

22. **Retrieve all posts tagged with 'Technology' along with their authors.**

23. **Find all users who have not placed any orders.**

---

## 🔹 Advanced JOIN Scenarios

24. **List all users along with the total amount they've spent on orders.**

25. **Retrieve the top 5 products by total sales amount.**

26. **Find the most active users based on the number of sessions they've initiated.**

---


```