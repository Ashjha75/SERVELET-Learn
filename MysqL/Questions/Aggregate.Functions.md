
---

## 📊 SQL Aggregate Functions: A Quick Reference

### 🔹 What Are Aggregate Functions?

Aggregate functions perform calculations on a set of values and return a single summarizing value. They are commonly used to:

* Summarize data (e.g., total sales, average ratings).
* Analyze grouped data (e.g., number of orders per user).
* Facilitate reporting and data analysis tasks.

---

### 🔹 Common Aggregate Functions

| Function  | Description                                   | Notes                                                                 |
| --------- | --------------------------------------------- | --------------------------------------------------------------------- |
| `COUNT()` | Returns the number of rows.                   | `COUNT(*)` includes all rows; `COUNT(column)` excludes `NULL` values. |
| `SUM()`   | Calculates the total sum of a numeric column. | Ignores `NULL` values.                                                |
| `AVG()`   | Computes the average of a numeric column.     | Ignores `NULL` values.                                                |
| `MIN()`   | Finds the smallest value in a column.         | Ignores `NULL` values.                                                |
| `MAX()`   | Finds the largest value in a column.          | Ignores `NULL` values.                                                |

---

### 🔹 Usage with `GROUP BY`

The `GROUP BY` clause groups rows that have the same values in specified columns into summary rows. Aggregate functions are often used with `GROUP BY` to perform calculations on each group.

**Example**: Retrieve the number of posts per user.

```sql
SELECT user_id, COUNT(*) AS post_count
FROM Posts
GROUP BY user_id;
```

---

### 🔹 Filtering with `HAVING`

While `WHERE` filters rows before grouping, `HAVING` filters groups after aggregation.

**Example**: Find users with more than 5 posts.

```sql
SELECT user_id, COUNT(*) AS post_count
FROM Posts
GROUP BY user_id
HAVING COUNT(*) > 5;
```

---

### 🔹 Additional Tips

* **NULL Handling**: Most aggregate functions ignore `NULL` values, except for `COUNT(*)`, which counts all rows.
* **Combining with Joins**: Aggregate functions can be used in conjunction with `JOIN` operations to analyze related data across multiple tables.
* **Nested Aggregations**: You can use subqueries to perform nested aggregations for more complex analyses.

---
```sql
# 📊 SQL Interview Questions: Aggregate Functions

---

## 🔹 Basic Aggregations

1. **Calculate the total number of users registered in the system.**

2. **Determine the average number of posts created per user.**

3. **Find the maximum number of comments made by a single user.**

4. **Identify the minimum number of tags associated with any post.**

5. **Compute the total revenue generated from all orders.**

---

## 🔹 Grouping and Aggregations

6. **List the number of posts created by each user.**

7. **Find the average number of comments per post for each user.**

8. **Determine the total number of orders placed by each user.**

9. **Calculate the total amount spent by each user on orders.**

10. **Identify the number of products ordered in each category.**

---

## 🔹 Filtering with HAVING

11. **List users who have created more than 5 posts.**

12. **Find posts that have received more than 10 comments.**

13. **Identify users who have spent more than $1000 on orders.**

14. **Determine products that have been ordered more than 50 times.**

15. **List categories with an average product price greater than $100.**

---

## 🔹 Advanced Aggregations

16. **Calculate the average number of tags per post.**

17. **Find the top 5 users with the highest number of comments.**

18. **Determine the month with the highest number of orders placed.**

19. **Compute the average order value per user.**

20. **Identify the product with the highest total sales revenue.**

---


```
