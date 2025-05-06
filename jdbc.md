# JDBC Essentials for Spring Boot and Interviews

## What is JDBC?

JDBC (Java Database Connectivity) is a standard Java API for connecting Java applications to relational databases. It allows Java programs to connect to a database, send SQL queries, retrieve results, and manipulate data. In practice, JDBC is the foundation for database access in Java – it provides the `java.sql` classes (like `DriverManager`, `Connection`, `Statement`, etc.) that let any Java app work with databases (MySQL, Oracle, PostgreSQL, etc.). Because JDBC abstracts the database-specific details, Java programs can switch databases more easily, and higher-level frameworks (like Spring’s `JdbcTemplate` or JPA/Hibernate) build on JDBC to simplify development.

## JDBC Architecture and Workflow

JDBC uses a driver-based, client-server architecture. The core workflow is: load a JDBC driver, obtain a `Connection`, create a `Statement` (or `PreparedStatement`), execute SQL, process the `ResultSet`, then close resources. In detail:

- **Load Driver (optional).** In older JDBC versions you call `Class.forName("com.mysql.cj.jdbc.Driver")` to register the driver. Modern JDBC drivers auto-register on the classpath, so this step is usually optional.
- **Get a Connection.** Use `DriverManager.getConnection(url, user, pass)` with the JDBC URL to connect. For example, `Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "pass")`.
- **Create Statement.** From the `Connection`, create a statement: either `Statement stmt = conn.createStatement()` for static SQL, or `PreparedStatement pstmt = conn.prepareStatement(sql)` for parameterized SQL.
- **Execute SQL.** If it’s a query, call `executeQuery()` which returns a `ResultSet`; if it’s an update/insert/delete, call `executeUpdate()` which returns an update count. A general `execute()` method returns a boolean indicating if a `ResultSet` was produced.
- **Process Results.** For a `SELECT`, iterate the `ResultSet` using `while(rs.next())` and retrieve columns with `getInt()`, `getString()`, etc. For updates, use the returned count.
- **Close Resources.** Finally, close the `ResultSet`, `Statement`, and `Connection` (typically in a `finally` block or try-with-resources) to free resources. Spring’s `JdbcTemplate` automatically handles resource cleanup to avoid leaks.

For example, a JDBC snippet might look like: instantiate `DriverManager` to connect, create a `Statement` to carry the SQL, and use a `ResultSet` to retrieve results in a loop. This core workflow is common to all JDBC code.

## Connecting to a Database

To establish a JDBC connection, supply the JDBC URL, username, and password to `DriverManager.getConnection()`. For example, connecting to a MySQL database:

```java
String url = "jdbc:mysql://localhost:3306/mydb";
String user = "root";
String password = "password";
try (Connection conn = DriverManager.getConnection(url, user, password)) {
    // Use the connection (e.g., create statements)
} catch (SQLException e) {
    e.printStackTrace();
}
```

This opens a connection to the database. In JDBC 4+ the driver class is loaded automatically when the JDBC URL is parsed, so explicitly calling `Class.forName("com.mysql.cj.jdbc.Driver")` is optional. When the connection succeeds, `conn` is a live session to the DB (analogous to a socket). Always handle `SQLException` (e.g., in a try-catch) and ensure the connection is closed (the try-with-resources above closes it automatically).

## CRUD Operations

Once connected, you can perform CRUD (Create, Read, Update, Delete) operations via SQL. Here are examples using both `Statement` and `PreparedStatement`:

- **SELECT (Read)** – Using a `Statement` to run a simple query:

    ```java
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT id, name, age FROM users");
    while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        System.out.println(id + ": " + name + " (" + age + ")");
    }
    rs.close();
    stmt.close();
    ```

- **INSERT (Create)** – Using a `PreparedStatement` with parameters:

    ```java
    String insertSql = "INSERT INTO users(name, age) VALUES (?, ?)";
    PreparedStatement pstmt = conn.prepareStatement(insertSql);
    pstmt.setString(1, "Alice");
    pstmt.setInt(2, 30);
    int inserted = pstmt.executeUpdate();
    System.out.println(inserted + " row(s) inserted.");
    pstmt.close();
    ```

- **UPDATE** – Example with `PreparedStatement`:

    ```java
    String updateSql = "UPDATE users SET age = ? WHERE id = ?";
    PreparedStatement upd = conn.prepareStatement(updateSql);
    upd.setInt(1, 31);
    upd.setInt(2, 1);
    int updated = upd.executeUpdate();
    System.out.println(updated + " row(s) updated.");
    upd.close();
    ```

- **DELETE** – Example with `Statement`:

    ```java
    Statement delStmt = conn.createStatement();
    int deleted = delStmt.executeUpdate("DELETE FROM users WHERE id = 2");
    System.out.println(deleted + " row(s) deleted.");
    delStmt.close();
    ```


These snippets illustrate the JDBC API calls for each operation. Notice how the `executeQuery` method is used for `SELECT` (returning a `ResultSet`), while `executeUpdate` is used for `INSERT/UPDATE/DELETE` (returning an integer count).

## PreparedStatement vs Statement

- **Statement:** Used for executing static SQL without parameters. You build a full query string and execute it with `stmt.executeQuery(...)` or `executeUpdate(...)`. This is simple but inflexible. It **cannot** accept input parameters, and if you concatenate user input into the SQL, it is vulnerable to SQL injection. Example:

    ```java
    String name = "O'Reilly"; // user input
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(
        "SELECT * FROM users WHERE name = '" + name + "'"
    );
    ```

  This kind of code would break or allow injection if `name` contains special characters.

- **PreparedStatement:** Precompiles the SQL with placeholders (`?`) for parameters. You set each parameter with setter methods (`setString`, `setInt`, etc.) before execution. Prepared statements are generally **faster** when reused (the DB can cache the query plan) and they automatically escape inputs, preventing SQL injection. Example:

    ```java
    String sql = "SELECT * FROM users WHERE name = ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, name);
    ResultSet rs = pstmt.executeQuery();
    ```

  Prepared statements are preferred whenever you have parameters or when running the same SQL multiple times. They offer better security and performance compared to raw `Statement`.


In summary, use **Statement** for one-off, static queries with no user input, and use **PreparedStatement** for any dynamic queries with parameters (and for batch operations). This avoids injection and often improves performance.

## Transaction Management

By default, each JDBC `Connection` starts in *auto-commit* mode, meaning every SQL statement is committed to the database as soon as it finishes. To group multiple statements into a transaction, you must disable auto-commit:

```java
conn.setAutoCommit(false);
try {
    // Perform multiple SQL updates/inserts/deletes
    stmt.executeUpdate("UPDATE account SET balance = balance - 100 WHERE id = 1");
    stmt.executeUpdate("UPDATE account SET balance = balance + 100 WHERE id = 2");
    conn.commit();  // commit both updates together
} catch (SQLException e) {
    conn.rollback();  // on error, undo all changes in this transaction
} finally {
    conn.setAutoCommit(true); // restore default if needed
}
```

Here, calling `conn.setAutoCommit(false)` tells JDBC not to commit until we explicitly call `conn.commit()`. If an error occurs, we call `conn.rollback()` to undo all changes since the last commit. This ensures atomicity of related operations. Remember to always reset auto-commit or close the connection when done. Spring’s transaction management (e.g. `@Transactional`) can automate these commit/rollback steps in a Spring Boot app.

## Connection Pooling (Basics)

Establishing a new database connection is relatively expensive (authentication, network handshake, etc.), so high-performance applications **reuse** connections through pooling. A connection pool maintains a fixed set of open connections; when the application needs a connection, it takes one from the pool, and returns it when done. This significantly **reduces latency** and resource usage compared to repeatedly opening/closing connections.

For example, without pooling, each request does:

> *“Every time a user request requires database access, a new connection must be established. Establishing a connection involves network latency, authentication, and resource allocation, all of which slow down performance”*.

With pooling, those costs are paid only occasionally. Common pools like HikariCP, Apache DBCP, or Tomcat JDBC manage connections under the hood. Spring Boot uses **HikariCP by default** (as long as Hikari is on the classpath) because it’s lightweight and high-performance. HikariCP pre-creates a small number of idle connections and keeps them alive, so when your code calls `getConnection()`, it’s fast. In Spring Boot, you typically just configure the pool via `application.properties`, but you can also instantiate it manually:

```java
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
config.setUsername("root");
config.setPassword("password");
// Optional: set pool size, timeouts, etc.
HikariDataSource dataSource = new HikariDataSource(config);
Connection conn = dataSource.getConnection(); // fetch a connection from the pool
```

The key point is that pooling improves performance and scalability by reusing connections. In high-traffic Spring Boot apps, HikariCP (or another pool) ensures there are always ready connections, reducing the overhead of connection creation.

## JDBC Limitations and Why Spring Boot Improves It

Raw JDBC has some limitations and boilerplate overhead:

- **Verbose Boilerplate:** Every query requires manual code to open connections, create statements, handle `SQLException`, iterate `ResultSet`, and close resources. This boilerplate is repetitive and error-prone (for example, forgetting to close a connection or statement).
- **Low-Level API:** JDBC works at the SQL level. There’s no automatic mapping between database tables and Java objects (like an ORM provides), and no caching or change tracking. Queries are strings, so typos or syntax errors only show up at runtime.
- **Error Handling:** All JDBC calls throw `SQLException` (checked exception), so Java code must catch or propagate many exceptions. Each `SQLException` is vendor-specific too.

Spring Boot (via Spring Framework) helps address these issues:

- **JdbcTemplate / Spring Data:** Spring’s `JdbcTemplate` class automates much of the JDBC workflow. It opens/queries/closes resources for you, so you just provide SQL and map result rows to objects. It “simplifies the use of JDBC since it handles the creation and release of resources”, which prevents common mistakes like forgetting to close a connection.
- **Transactions:** Spring’s `DataSourceTransactionManager` and `@Transactional` annotation manage commit/rollback automatically, so you rarely need to call `commit()` or `rollback()` manually in application code.
- **DataSource Auto-Configuration:** Spring Boot auto-configures a `DataSource` (backed by a pool like HikariCP) based on properties, so you don’t have to write any code to set up the pool.
- **Higher-Level Abstractions:** Spring Data JPA (Hibernate) or Spring Data JDBC provide object-relational mapping on top of JDBC, further reducing boilerplate (although at the cost of understanding JPA). These abstractions free you from writing most SQL and JDBC code directly, while still allowing custom queries as needed.

In summary, Spring Boot **reduces JDBC boilerplate** and manages resources for you, making database access easier, safer, and more aligned with modern practices.

## Common JDBC Interview Questions

1.  **What is JDBC?**  
    JDBC (Java Database Connectivity) is the Java API for connecting and executing queries with relational databases. It provides classes and interfaces (in `java.sql` and `javax.sql`) to connect to a database, send SQL commands, and process results, enabling database-agnostic Java programs.

2.  **How do you establish a JDBC connection?**  
    You call `DriverManager.getConnection(dbUrl, username, password)` with the JDBC URL (e.g. `"jdbc:mysql://localhost:3306/mydb"`). This returns a `Connection` object. Optionally, older code calls `Class.forName("com.mysql.cj.jdbc.Driver")` first to load the driver, but modern drivers do this automatically. Example steps: register/load driver, then `Connection conn = DriverManager.getConnection(url, user, pass)`.

3.  **What is the role of DriverManager?**  
    `DriverManager` is a factory class that manages JDBC drivers and establishes connections. When a driver is loaded, it registers itself with `DriverManager`. Then, `DriverManager.getConnection()` uses the registered drivers to return a `Connection` for the given URL. Essentially, `DriverManager` selects the correct driver implementation to handle the connection request.

4.  **What is the difference between Statement and PreparedStatement?**  
    A `Statement` executes a static SQL string. It’s useful for simple or one-off queries without parameters. A `PreparedStatement` pre-compiles the SQL and allows parameter binding (`?` placeholders). Prepared statements are more efficient when reused and *prevent SQL injection*, since they safely escape parameter values. In short: use `Statement` for simple fixed queries, and `PreparedStatement` for any query with user-supplied parameters or repeated execution.

5.  **How do you execute SELECT vs INSERT/UPDATE/DELETE in JDBC?**  
    Use `executeQuery(sql)` for `SELECT` statements; it returns a `ResultSet`. For `INSERT`, `UPDATE`, or `DELETE`, use `executeUpdate(sql)`; it returns an integer indicating the number of affected rows. (There is also `execute(sql)`, which returns a boolean indicating whether the result is a `ResultSet`.) In practice, call `ResultSet rs = stmt.executeQuery("SELECT ...")` for reads, and `int count = stmt.executeUpdate("UPDATE ...")` for writes.

6.  **What is a ResultSet?**  
    A `ResultSet` represents the data returned by a `SELECT` query. It functions like a cursor over the result rows. You call `rs.next()` to move to the next row (initially positioned before the first row). Column values are retrieved by methods like `getInt("colName")` or `getString(index)`. The `ResultSet` is forward-only by default and must be closed when done. Note that even if a query returns no rows, `executeQuery()` still returns a `ResultSet` (you just get no rows when iterating).

7.  **How do transactions work in JDBC?**  
    By default, JDBC connections are in *auto-commit* mode, meaning each SQL statement is committed immediately. To group statements in a transaction, disable auto-commit with `conn.setAutoCommit(false)`. Then execute your SQL statements, and call `conn.commit()` to apply them as a unit. If an error occurs, call `conn.rollback()` to undo all changes since the last commit. This manual commit/rollback handling is how you ensure atomic transactions. In Spring Boot, the framework can manage this via `@Transactional` annotations instead.

8.  **What is auto-commit mode?**  
    Auto-commit mode (on by default) means each SQL statement is treated as its own transaction and is committed immediately after it finishes. This is simple but rarely what you want for multi-step operations. You turn it off (`setAutoCommit(false)`) to manually control when commits happen.

9.  **Why use connection pooling?**  
    Opening a new database connection is costly (network round-trip, authentication, etc.), so creating many connections degrades performance. Connection pooling keeps a set of open connections ready to use, which reduces latency and resource usage. Pooled connections are reused by the application (returned to the pool after use). This is especially important under load. As an example, without pooling, *“establishing a connection involves network latency, authentication, and resource allocation, all of which can slow down performance”*.

10. **What is DataSource?**  
    `DataSource` (in `javax.sql`) is an alternative to `DriverManager` for getting connections. It often represents a connection pool or factory. A `DataSource` can offer extra features (caching, timeout, logging, pooling) and can be configured externally (e.g. via JNDI). Spring Boot auto-configures a pooled `DataSource` (usually HikariCP) for you. In short, use a `DataSource` in production for pooling and better management, rather than calling `DriverManager` directly.

11. **How do you handle SQL exceptions and resource cleanup?**  
    JDBC methods throw `SQLException` for any database error. You should catch or declare this exception. It’s critical to clean up resources (close `ResultSet`, `Statement`, `Connection`) to avoid leaks. Best practice is using **try-with-resources** (Java 7+) so JDBC objects are closed automatically. For example: `try (Connection c = DriverManager.getConnection(...); Statement s = c.createStatement()) { ... }`. Spring’s `JdbcTemplate` further simplifies this by catching exceptions and automatically closing resources.

12. **What is SQL injection and how does JDBC prevent it?**  
    SQL injection happens when untrusted input is concatenated into SQL. To prevent it, use `PreparedStatement` with parameter binding. The JDBC driver will escape input properly. For example, `pstmt.setString(1, userInput)` ensures special characters in `userInput` don’t break the query syntax. Avoid building SQL strings by concatenation when user data is involved.


These questions and answers cover the essential JDBC knowledge for working with Spring Boot and preparing for technical interviews. For more practice, review official JDBC tutorials and experiment with code examples.