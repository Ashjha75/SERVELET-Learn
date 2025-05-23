
---

## 🧾 What is a SQL View?

A **SQL View** is a virtual table that represents the result of a stored query. It does not store data physically but provides a way to simplify complex queries, enhance security, and present data in a customized format. ([GeeksforGeeks][1])

---

## 🛠️ Key Characteristics of Views

* **Virtual Table**: Behaves like a table but does not store data physically.
* **Simplifies Complex Queries**: Encapsulates complex joins and conditions into a single object.
* **Enhances Security**: Restricts access to specific columns or rows.
* **Customizes Data Presentation**: Provides tailored data views for different users.
* **Dynamic Data Retrieval**: Reflects the current data from the underlying tables each time it's accessed.([GeeksforGeeks][1])

---

## 🧱 Types of Views

1. **Simple View**: Based on a single table without any group functions.
2. **Complex View**: Based on multiple tables and can include group functions.
3. **Materialized View**: Stores the result of the query physically and needs to be refreshed to reflect changes in the underlying tables. ([Wikipedia][2])

---

## 🧾 Creating a View

**Syntax**:

```sql
CREATE VIEW view_name AS
SELECT column1, column2, ...
FROM table_name
WHERE condition;
```

**Example**: Creating a view to list active users.

```sql
CREATE VIEW ActiveUsers AS
SELECT id, name, email
FROM Users
WHERE status = 'active';
```



---

## 🔄 Updating a View

Views can be updated if they are based on a single table and do not include group functions or DISTINCT.

**Syntax**:

```sql
CREATE OR REPLACE VIEW view_name AS
SELECT column1, column2, ...
FROM table_name
WHERE condition;
```



---

## ❌ Dropping a View

**Syntax**:

```sql
DROP VIEW view_name;
```



---

## 🔐 Security and Permissions

Views can be used to restrict access to specific data. By granting permissions on the view and not on the underlying tables, users can access only the data exposed by the view.([Microsoft Learn][3])

---

## 📌 Use Cases in Your Schema

* **Simplify Complex Joins**: Create views that join `Users`, `Posts`, and `Comments` to present user activity.
* **Restrict Sensitive Data**: Expose only necessary columns from the `Users` table to certain roles.
* **Aggregate Data**: Use views to present summarized order information from `Orders` and `Order_Items`.

---
