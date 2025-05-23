
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

