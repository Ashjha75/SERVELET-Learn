
---

## 🔄 What is a Transaction?

A **transaction** is a sequence of one or more SQL operations executed as a single unit of work. The primary goal is to ensure data integrity and consistency, even in cases of system failures or concurrent access.([InfoLab][1])

---

## 🧱 ACID Properties

Transactions adhere to the **ACID** properties to maintain database reliability:([Wikipedia][2])

* **Atomicity**: Ensures that all operations within a transaction are completed; if not, the transaction is aborted, and the database remains unchanged.([GeeksforGeeks][3])

* **Consistency**: Guarantees that a transaction transforms the database from one valid state to another, maintaining all predefined rules, such as constraints and triggers.([Wikipedia][2])

* **Isolation**: Ensures that concurrent transactions do not interfere with each other, preserving data integrity.

* **Durability**: Once a transaction is committed, the changes are permanent, even in the event of a system failure.

---

## 🛠️ Transaction Control Commands

* **BEGIN TRANSACTION** or **START TRANSACTION**: Initiates a new transaction.

* **COMMIT**: Saves all changes made during the transaction.

* **ROLLBACK**: Reverts all changes made during the transaction.

* **SAVEPOINT**: Sets a point within a transaction to which you can later roll back.

* **RELEASE SAVEPOINT**: Removes a previously defined savepoint.

* **SET TRANSACTION**: Establishes characteristics for the transaction, such as isolation level.

---

## 🔄 Transaction States

A transaction progresses through several states:

1. **Active**: The initial state where operations are being executed.([GeeksforGeeks][3])

2. **Partially Committed**: After the final operation has been executed.

3. **Committed**: After successful completion, and changes are permanently saved.

4. **Failed**: If an error occurs, leading to an abort.

5. **Aborted**: After the transaction has been rolled back.

---

## 🔐 Isolation Levels

Isolation levels determine how transaction integrity is visible to other users and systems:([UC Davis Computer Science][4])

* **Read Uncommitted**: Allows reading uncommitted changes, leading to potential dirty reads.

* **Read Committed**: Prevents dirty reads by ensuring only committed data is read.([Wikipedia][5])

* **Repeatable Read**: Ensures that if a row is read twice in the same transaction, the data remains the same.

* **Serializable**: The highest isolation level, ensuring complete isolation from other transactions.

---

## 📌 Use Cases in Your Schema

* **Order Processing**: Ensuring that all steps in order placement, such as inventory deduction and payment processing, are completed successfully.

* **User Registration**: Guaranteeing that user data is fully inserted across multiple tables or not at all.

* **Inventory Management**: Maintaining accurate stock levels during concurrent transactions.

---
