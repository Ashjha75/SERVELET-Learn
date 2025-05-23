---

## 🧱 **Normalization: Structuring Data Efficiently**

Normalization is the process of organizing data in a database to reduce redundancy and improve data integrity. It involves decomposing a database into multiple related tables.

### **1. First Normal Form (1NF)**

* **Atomicity**: Each column contains indivisible values (no lists or sets).
* **Uniqueness**: Each row is unique; no duplicate rows.
* **Column Uniqueness**: Each column has a unique name.
* **Order Irrelevance**: The order of data does not matter.

*Example*: A table with a column storing multiple phone numbers violates 1NF. Each phone number should be in a separate row.

### **2. Second Normal Form (2NF)**

* **1NF Compliance**: The table must first satisfy 1NF.
* **No Partial Dependency**: All non-key attributes must depend on the entire primary key. This applies to tables with composite primary keys.

*Example*: In a table with a composite key (StudentID, CourseID), if the StudentName depends only on StudentID, it violates 2NF. To normalize, move StudentName to a separate table.

### **3. Third Normal Form (3NF)**

* **2NF Compliance**: The table must first satisfy 2NF.
* **No Transitive Dependency**: Non-key attributes should not depend on other non-key attributes.

*Example*: If a table has attributes (StudentID, StudentName, DepartmentName, DepartmentHead), and DepartmentHead depends on DepartmentName, it violates 3NF. To normalize, move DepartmentName and DepartmentHead to a separate table.

### **4. Boyce-Codd Normal Form (BCNF)**

* **3NF Compliance**: The table must first satisfy 3NF.
* **No Non-Trivial Functional Dependencies**: Every determinant must be a candidate key.

*Example*: In a table with attributes (StudentID, CourseID, Instructor), if Instructor depends on CourseID, it violates BCNF. To normalize, create a separate table for instructors.

### **5. Fourth Normal Form (4NF)**

* **BCNF Compliance**: The table must first satisfy BCNF.
* **No Multi-Valued Dependencies**: A multi-valued dependency occurs when one attribute determines multiple independent attributes.

*Example*: A table with attributes (StudentID, Language, Sport) violates 4NF if a student can speak multiple languages and play multiple sports. To normalize, create separate tables for languages and sports.

### **6. Fifth Normal Form (5NF)**

* **4NF Compliance**: The table must first satisfy 4NF.
* **No Join Dependency**: A table is in 5NF if it cannot be decomposed into smaller tables without losing information.

*Example*: A table with attributes (StudentID, CourseID, InstructorID) violates 5NF if a student can enroll in multiple courses with multiple instructors. To normalize, create separate tables for course enrollments and instructor assignments.

---

## 🔗 **Foreign Keys (FKs): Ensuring Data Integrity**

A **Foreign Key** is a column or a set of columns in one table that uniquely identifies a row of another table or the same table (self-reference). It establishes and enforces a link between the data in two tables.

### **Purpose of Foreign Keys**

* **Referential Integrity**: Ensures that a foreign key value always points to an existing, valid row in the referenced table.
* **Data Consistency**: Prevents actions that would leave orphaned records in the database.

### **Defining Foreign Keys**

* **Syntax**:

  ```sql
  CREATE TABLE child_table (
      id INT PRIMARY KEY,
      parent_id INT,
      FOREIGN KEY (parent_id) REFERENCES parent_table(id)
  );
  ```
* **Referential Actions**:

    * `ON DELETE CASCADE`: Deletes rows in the child table when the corresponding row in the parent table is deleted.
    * `ON DELETE SET NULL`: Sets the foreign key column in the child table to NULL when the corresponding row in the parent table is deleted.
    * `ON UPDATE CASCADE`: Updates the foreign key column in the child table when the corresponding row in the parent table is updated.
    * `ON UPDATE SET NULL`: Sets the foreign key column in the child table to NULL when the corresponding row in the parent table is updated.

*Example*: In your schema, the `Posts` table has a foreign key `user_id` referencing the `Users` table. This ensures that each post is associated with a valid user.

---

## 🧩 **Applying Normalization & FKs to Your Schema**

Here's how normalization and foreign keys apply to your schema:

* **Users Table**: Contains user information. The primary key is `id`.
* **Profiles Table**: Contains additional user details. The foreign key `user_id` references `Users(id)`.
* **Posts Table**: Contains user-generated posts. The foreign key `user_id` references `Users(id)`.
* **Comments Table**: Contains comments on posts. The foreign keys `post_id` and `user_id` reference `Posts(id)` and `Users(id)`, respectively.
* **Categories Table**: Contains categories for posts. The primary key is `id`.
* **Tags Table**: Contains tags for posts. The primary key is `id`.
* **Post\_Tags Table**: Links posts and tags. Contains foreign keys `post_id` and `tag_id` referencing `Posts(id)` and `Tags(id)`, respectively.
* **Products Table**: Contains product information. The primary key is `id`.
* **Orders Table**: Contains user orders. The foreign key `user_id` references `Users(id)`.
* **Order\_Items Table**: Contains items in orders. The foreign keys `order_id` and `product_id` reference `Orders(id)` and `Products(id)`, respectively.
* **Payments Table**: Contains payment information. The foreign key `order_id` references `Orders(id)`.
* **Sessions Table**: Contains user session data. The foreign key `user_id` references `Users(id)`.
* **Audit\_Logs Table**: Contains logs of changes. The foreign key `user_id` references `Users(id)`.

---

```sql
# 🧩 SQL Interview Questions: Normalization & Foreign Keys

---

## 🔹 Normalization

1. **Identify any violations of the First Normal Form (1NF) in the `Users` table. Suggest modifications to achieve 1NF compliance.**

2. **Examine the `Posts` table for partial dependencies that violate the Second Normal Form (2NF). Propose a normalized structure.**

3. **Determine if the `Profiles` table has any transitive dependencies violating the Third Normal Form (3NF). How would you restructure the table?**

4. **Assess whether the `Order_Items` table satisfies the Boyce-Codd Normal Form (BCNF). Justify your answer.**

5. **In the `Posts` table, if `category_name` is stored alongside `category_id`, does this violate normalization principles? Explain and suggest a solution.**

6. **Evaluate the `Post_Tags` junction table for redundancy. How does its design support normalization?**

7. **If the `Audit_Logs` table stores both `user_name` and `user_id`, discuss the normalization implications and recommend changes.**

8. **Consider a scenario where the `Comments` table includes both `post_title` and `post_id`. Analyze the normalization concerns and propose a remedy.**

---

## 🔹 Foreign Keys

9. **List all foreign key constraints present in the `Orders` table. Describe the relationships they enforce.**

10. **Explain how the foreign key in the `Comments` table ensures referential integrity with the `Posts` table.**

11. **Discuss the implications of deleting a user from the `Users` table on the `Posts` and `Comments` tables. How can ON DELETE actions be utilized to maintain data integrity?**

12. **Describe how a foreign key in the `Payments` table links to the `Orders` table. What are the benefits of this relationship?**

13. **In the `Order_Items` table, how do foreign keys to both `Orders` and `Products` tables facilitate data consistency?**

14. **What would be the consequences of removing the foreign key constraint between `Posts` and `Users`? How would this affect data integrity?**

15. **Propose a method to enforce a self-referential foreign key in the `Users` table to represent a referral system.**

16. **How can cascading updates be implemented between the `Categories` and `Posts` tables to reflect category name changes?**

17. **If a `Product` is deleted, how can ON DELETE SET NULL be used in the `Order_Items` table to handle the orphaned records?**

18. **Design a foreign key constraint for the `Sessions` table to link each session to a valid user in the `Users` table.**

19. **Explain the role of foreign keys in the `Post_Tags` table in establishing many-to-many relationships.**

20. **How can foreign key constraints be used to prevent insertion of a `Comment` for a non-existent `Post`?**

---


```