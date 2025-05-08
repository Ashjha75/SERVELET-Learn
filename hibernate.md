# Hibernate Interview Notes

## ORM Basics and Hibernate Introduction

* **ORM (Object-Relational Mapping)** maps Java objects to database tables. Hibernate is a popular Java ORM framework
  that implements the JPA (Java Persistence API) specification. It provides a framework to map application domain
  objects to database tables and vice versa, eliminating boilerplate JDBC code.
* **Benefits:** Hibernate removes JDBC boilerplate, handles transactions, and supports associations, inheritance and
  polymorphism in queries. It is database-agnostic, open-source, and integrates easily with frameworks like Spring.
  Hibernate also supports caching and lazy loading to improve performance.
* **Example Mapping:** Use JPA annotations to map a class to a table. For instance:

  ```java
  @Entity
  @Table(name = "EMPLOYEE")
  public class Employee {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      @Column(name = "emp_name")
      private String name;
      // getters/setters...
  }
  ```

  This makes `Employee` a persistent entity with an auto-generated primary key.

## Hibernate Architecture

* **Configuration:** The `Configuration` class (in `org.hibernate.cfg`) reads Hibernate settings (e.g.
  `hibernate.cfg.xml`) and mapping metadata. Calling `cfg.configure()` loads settings and builds a `SessionFactory`.
* **SessionFactory (`org.hibernate.SessionFactory`)** is a thread-safe, immutable cache of compiled mappings for one
  database. It is created once at application startup and used to open `Session` instances. It may also hold a
  second-level (shared) cache of entity data.
* **Session (`org.hibernate.Session`)** is a single-threaded, short-lived object representing a conversation with the
  database. It wraps a JDBC connection and manages a first-level cache of persistent objects within a transaction. Use
  `session = sessionFactory.openSession()` (or `getCurrentSession()`) to obtain a `Session`.
* **Transaction (`org.hibernate.Transaction`)** abstracts the underlying JDBC or JTA transaction. You start it with
  `session.beginTransaction()`, then `commit()` or `rollback()`. Hibernate requires operations to be within a
  transaction.

## Hibernate Configuration (XML and Annotations)

* **XML Configuration:** Traditional Hibernate uses `hibernate.cfg.xml` (in classpath) to specify JDBC URL, driver,
  dialect, and mapping resources. The `<session-factory>` section includes `<property>` entries (e.g.
  `hibernate.dialect`, `hbm2ddl.auto`) and mapping file references.
* **Annotation Configuration:** With JPA annotations, you may not need XML mappings. In code, you can configure
  Hibernate programmatically:

  ```java
  Configuration cfg = new Configuration();
  cfg.configure();  // loads hibernate.cfg.xml by default
  SessionFactory factory = cfg.buildSessionFactory();
  ```

  As noted, `Configuration` “activates Hibernate” by reading config and mapping files and building metadata.
* **Spring Boot:** In Spring Boot, you typically skip `hibernate.cfg.xml`. Instead, use `application.properties` (or
  YAML) with properties like `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto`, etc. Spring Boot auto-configures
  Hibernate (via JPA) when you include `spring-boot-starter-data-jpa`. For example, setting:

  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/mydb
  spring.jpa.hibernate.ddl-auto=update
  ```

  tells Hibernate to connect to the DB and auto-update the schema.

## Session and SessionFactory

* **SessionFactory** is built once (typically at startup). It is **thread-safe and immutable**, so you can share it
  across threads. It holds compiled mappings and optional second-level cache. To obtain it:

  ```java
  Configuration cfg = new Configuration().configure();
  SessionFactory sessionFactory = cfg.buildSessionFactory();
  ```
* **Session** is obtained from the `SessionFactory`. It is **not thread-safe** and should be used within a single
  thread/transaction. Example usage:

  ```java
  Session session = sessionFactory.openSession();
  Transaction tx = session.beginTransaction();
  // ... perform CRUD ...
  tx.commit();
  session.close();
  ```

## CRUD Operations

* **Create:** Use `session.save(entity)` or `session.persist(entity)` inside a transaction. Example:

  ```java
  Session session = sessionFactory.openSession();
  Transaction tx = session.beginTransaction();
  session.save(new Employee("Alice"));
  tx.commit();
  session.close();
  ```
* **Read:** Use `session.get(EntityClass.class, id)` to immediately fetch an object by primary key (returns `null` if
  not found), or `session.load(EntityClass.class, id)` to get a proxy.
* **Update:** Either modify a fetched entity inside a transaction (Hibernate tracks changes) or use
  `session.update(entity)`/`session.merge(entity)` for detached objects.
* **Delete:** Call `session.delete(entity)` to remove a persistent object from the database.
* All operations should be within a `Transaction`. Hibernate automatically flushes changes on `commit()`, and exceptions
  cause rollbacks.

## Entity Mapping Annotations

Hibernate supports JPA annotations (and some Hibernate-specific). Key annotations include:

* **@Entity:** Marks a Java class as a persistent entity.
* **@Table(name="TABLE\_NAME"):** (Optional) Specifies the DB table name for the entity.
* **@Id:** Marks the primary key field.
* **@GeneratedValue:** Configures primary key generation strategy (`AUTO`, `IDENTITY`, etc.).
* **@Column(name="COL\_NAME"):** (Optional) Specifies the column name (and constraints) for a field.
* **@EmbeddedId / @Embeddable:** For composite keys and embeddable value types.
* **@OneToOne, @OneToMany, @ManyToOne, @ManyToMany:** Define relationships between entities. Often used with `mappedBy`,
  `@JoinColumn`, or `@JoinTable` to configure foreign key columns.
* **@JoinColumn:** Specifies the foreign key column for single-valued associations.
* **@JoinTable:** Specifies the join table for many-to-many.
* **@Cascade** (Hibernate-specific) and **cascade** attribute in JPA to configure cascading of operations.

  *Example: One-to-One mapping snippet:*

  ```java
  @Entity
  public class Address {
      @Id @GeneratedValue
      private Long id;
      private String street;
      @OneToOne(mappedBy = "address")
      private Employee employee;
      // ...
  }

  @Entity
  public class Employee {
      @Id @GeneratedValue
      private Long id;
      @OneToOne
      @JoinColumn(name = "address_id")
      private Address address;
      // ...
  }
  ```

## Primary Key Generation Strategies

JPA supports four main `GenerationType` strategies for `@GeneratedValue`:

1. **AUTO:** (Default) Hibernate chooses the strategy (usually sequence or table).
2. **IDENTITY:** Uses database identity/auto-increment columns. Simple but can be less performant.
3. **SEQUENCE:** Uses a DB sequence object to generate unique IDs (often best for Oracle/Postgres).
4. **TABLE:** Simulates a sequence in a separate table (rarely used today).

For example:

```java

@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
private Long id;
```

You can also customize sequences or use Hibernate’s `@GenericGenerator` for custom strategies.

## Relationships: One-to-One, One-to-Many, Many-to-Many

* **One-to-One:** Each row in A relates to one row in B. Map with `@OneToOne`. Use `@JoinColumn` (foreign key in one
  table) or shared primary key.
* **One-to-Many / Many-to-One:** Standard parent-child relationship. For example, one `Customer` has many `Order`s. In
  `Customer` class:

  ```java
  @OneToMany(mappedBy = "customer")
  private Set<Order> orders;
  ```

  In `Order` class:

  ```java
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;
  ```

  This setup means a foreign key in `Order` references `Customer`.
* **Many-to-Many:** Both sides have collections of each other, connected by a join table. Example:

  ```java
  @ManyToMany
  @JoinTable(name = "EMP_PROJECT",
      joinColumns = @JoinColumn(name = "emp_id"),
      inverseJoinColumns = @JoinColumn(name = "project_id"))
  private Set<Project> projects;
  ```

  Hibernate manages an intermediate table (`EMP_PROJECT`) holding pairs of IDs. A many-to-many relationship is
  implemented using such a join table.

*(Illustration: In many-to-many, e.g. `Employee` and `Project` tables relate via an `Employee_Project` join table.)*

## Cascade Types and Fetch Types

* **Cascade Types:** Control how operations propagate to associated entities. Common options include:
  `CascadeType.ALL` (all operations), `PERSIST`, `MERGE`, `REMOVE`, `REFRESH`, `DETACH`. For example,
  `@OneToMany(cascade = CascadeType.ALL)` means that saving or deleting a parent automatically does so for its children.
* **Fetch Types:** Define loading strategy for associations. Two options:

    * **LAZY (default for collections):** Associated data is loaded only on first access. Efficient if you don’t always
      need the related entities. Hibernate uses proxies or bytecode enhancement to delay loading until needed.
    * **EAGER:** Associated data is loaded immediately along with the parent entity. Useful when the child is always
      needed, but can hurt performance if overused. In JPA, `@OneToMany` and `@ManyToMany` default to LAZY, while
      `@ManyToOne` and `@OneToOne` default to EAGER.

  *Example:*

  ```java
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Order> orders;
  ```

## HQL (Hibernate Query Language)

* **HQL** is Hibernate’s object-oriented query language. It looks like SQL but operates on entity classes and their
  properties, not tables and columns.
* HQL supports polymorphism and associations. For example:

  ```java
  List<Employee> list = session.createQuery(
      "FROM Employee e WHERE e.name = :nm", Employee.class)
      .setParameter("nm", "Alice")
      .getResultList();
  ```
* HQL queries are validated at startup if defined as named queries, and they are database-independent (Hibernate
  translates them to SQL).

## Criteria API and JPQL

* **Criteria API:** A programmatic, typesafe way to build queries (introduced in JPA 2). You use `CriteriaBuilder` and
  `CriteriaQuery` instead of writing JPQL strings. It allows dynamic query construction without string concatenation.
  Hibernate’s Criteria (legacy) was replaced by the JPA `CriteriaBuilder`. For example:

  ```java
  CriteriaBuilder cb = session.getCriteriaBuilder();
  CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
  Root<Employee> e = q.from(Employee.class);
  q.select(e).where(cb.equal(e.get("name"), "Bob"));
  List<Employee> result = session.createQuery(q).getResultList();
  ```

  As noted, the Criteria API is more object-oriented and supports projections, joins, and ordering.
* **JPQL:** Java Persistence Query Language (standardized) is string-based like HQL. It is part of JPA. Example:

  ```java
  List<Employee> emps = entityManager.createQuery(
      "SELECT e FROM Employee e WHERE e.salary > 5000", Employee.class)
      .getResultList();
  ```

  JPQL vs HQL: HQL is Hibernate’s implementation, JPQL is the JPA spec. They are very similar syntactically.

## Named Queries

* Define reusable queries by name, either in annotations (`@NamedQuery`, `@NamedNativeQuery`) on entities or in XML
  mapping. These are parsed at startup (fail-fast on errors) and can be referred by name later.
* *Example:*

  ```java
  @Entity
  @NamedQuery(name = "Employee.findByName",
              query = "FROM Employee e WHERE e.name = :nm")
  public class Employee { … }
  ```

  You can then do:

  ```java
  Query<Employee> q = session.createNamedQuery("Employee.findByName", Employee.class);
  q.setParameter("nm", "Alice");
  List<Employee> emps = q.getResultList();
  ```
* Advantages of named queries: Centralized definition, reuse across the app, and validation at startup.

## Hibernate Caching (First-Level and Second-Level)

* **First-Level Cache:** This is the session cache. Every `Session` instance has its own cache of objects it has already
  loaded. Within a transaction, repeated `get()` calls for the same ID won’t hit the database again, thanks to this
  cache. The first-level cache is mandatory and cannot be turned off.
* **Second-Level Cache:** Optional cache at the `SessionFactory` level. If enabled (
  `hibernate.cache.use_second_level_cache=true`) and configured with a provider (e.g. Ehcache, Hazelcast, Infinispan),
  Hibernate can store entity state across sessions. This improves performance for read-mostly data shared across
  transactions.
* **Query Cache:** A supplemental cache to store query result sets. Must be enabled separately (
  `hibernate.cache.use_query_cache=true`) and configured on specific queries (`query.setCacheable(true)`).
* *Note:* Caching can greatly speed up reads but adds complexity (invalidation, memory).

## Lazy vs Eager Loading

* **Lazy Loading:** The default for collections (`@OneToMany`, `@ManyToMany`). Hibernate will load associated entities
  on demand (when you access the collection) rather than immediately. This avoids unnecessary data fetching if you don’t
  use the relationship.
* **Eager Loading:** Loads associations immediately with the parent entity. Useful if you always need the related data,
  but can degrade performance if the associations are large or deeply nested.
* By default in JPA, `@ManyToOne` and `@OneToOne` are EAGER, while `@OneToMany` and `@ManyToMany` are LAZY. You can
  override with `fetch = FetchType.LAZY` or `EAGER`.

## Transaction Management

* Hibernate requires a transaction for most operations. Use `session.beginTransaction()` to start, then `tx.commit()` or
  `tx.rollback()`. As noted, **Hibernate automatically rolls back the transaction if a runtime exception occurs**.
* In **Spring**, transaction management is usually done declaratively. For example, `@Transactional` on a service
  class/method will open/commit/rollback transactions automatically using Spring’s `PlatformTransactionManager`.
* Example usage without Spring:

  ```java
  Session session = sessionFactory.openSession();
  Transaction tx = session.beginTransaction();
  try {
      // perform DB operations
      tx.commit();
  } catch(Exception ex) {
      tx.rollback();
  } finally {
      session.close();
  }
  ```

## `get()` vs `load()`

* `session.get(Entity.class, id)`: Immediately hits the database and returns the entity or `null` if not found.
* `session.load(Entity.class, id)`: Returns a **proxy** without hitting the database unless required. Throws
  `ObjectNotFoundException` if the entity does not exist when you access its properties.
* Use `get()` when you want to check existence (`null` vs exception). Use `load()` when you are sure the object exists
  and want potential performance gain (lazy proxy).
  Example:

  ```java
  Employee e1 = session.get(Employee.class, 1L);  // hits DB now
  Employee e2 = session.load(Employee.class, 2L); // proxy returned
  ```

## Native SQL in Hibernate

* Hibernate can execute raw SQL queries if needed, e.g. for vendor-specific features. Use
  `session.createNativeQuery(sql)` (or the older `createSQLQuery`) to run SQL. You can map results to entities or
  scalars. For example:

  ```java
  List<Employee> emps = session.createNativeQuery(
      "SELECT * FROM EMPLOYEE", Employee.class)
      .getResultList();
  ```
* Named native queries can also be defined via `@NamedNativeQuery`. This lets you keep complex SQL in a named
  definition (centrally) similar to HQL named queries.

## Auto schema generation (`hbm2ddl.auto`)

* The Hibernate `hbm2ddl.auto` (or Spring’s `spring.jpa.hibernate.ddl-auto`) setting controls automatic database schema
  actions. Common values:

    * `none` (default in Spring Boot for non-embedded DB): do nothing.
    * `validate`: Validate that the schema matches the entities (no changes made).
    * `update`: Compare entity mappings to schema and perform SQL `ALTER` to update schema.
    * `create`: Drop existing schema and create a new one on startup.
    * `create-drop`: Like `create`, but also drop schema when SessionFactory closes.
* In **production**, it’s recommended to use `none` or `validate` and manage schema via migration tools (
  Flyway/Liquibase). Spring Boot defaults to `create-drop` for embedded DBs and `none` for others.

## Integration with Spring Boot (Overview)

* **Auto-configuration:** Spring Boot’s `spring-boot-starter-data-jpa` includes Hibernate by default. It auto-configures
  a `LocalContainerEntityManagerFactoryBean` (backed by Hibernate’s `SessionFactory`) and a transaction manager. You
  generally only need to configure `spring.datasource.*` and `spring.jpa.*` properties.
* **Entity Scanning:** Annotate your application class with `@SpringBootApplication` (which includes
  `@EnableAutoConfiguration` and `@ComponentScan`). Spring Boot will scan `@Entity` classes (in the same or
  sub-packages) and create tables (if `ddl-auto` is set).
* **Repositories:** Use Spring Data JPA repositories (e.g. extending `JpaRepository`) for CRUD. Example:

  ```java
  @Repository
  public interface EmployeeRepository extends JpaRepository<Employee, Long> { }
  ```

  This gives you methods like `save()`, `findById()`, etc., without writing SQL.
* **Properties:** Spring Boot property `spring.jpa.hibernate.ddl-auto` is passed to Hibernate’s `hbm2ddl.auto` (as
  described above). Boot chooses defaults (create-drop for H2/Derby in-memory, none for others).
* **Transactions:** In Spring Boot, simply using `@Transactional` on service methods will manage Hibernate transactions
  for you (commit/rollback) with no manual code.

## Common Hibernate Interview Questions

1. **What is Hibernate?** – Hibernate is a Java-based ORM framework that implements JPA. It maps Java objects to
   database tables and automates CRUD operations. It provides features like caching, lazy loading, and a query
   language (HQL).
2. **What is the difference between SessionFactory and Session?** – `SessionFactory` is a heavyweight, thread-safe
   factory for `Session` instances. It is created once per application. A `Session` is a lightweight, non-thread-safe
   object that represents a unit of work with the database.
3. **How do you map entity classes?** – Use JPA annotations such as `@Entity`, `@Table`, `@Id`, `@GeneratedValue`,
   `@OneToMany`, etc., to map classes and fields to tables and columns. For example,
   `@Entity @Table(name="EMP") class Emp { @Id Long id; }`.
4. **Explain primary key generation strategies.** – The `@GeneratedValue` annotation can use `AUTO`, `IDENTITY`,
   `SEQUENCE`, or `TABLE` strategies. AUTO lets Hibernate pick, IDENTITY uses auto-increment, SEQUENCE uses a DB
   sequence, and TABLE uses a separate table to simulate a sequence.
5. **What is the difference between `get()` and `load()`?** – `get()` hits the database immediately and returns the
   object or `null` if not found. `load()` returns a proxy without initial DB access; it throws an exception if the
   entity doesn’t exist when accessed.
6. **What are Cascade types?** – Cascade types (`ALL`, `PERSIST`, `MERGE`, `REMOVE`, etc.) define which operations
   should cascade from parent to child entities. For example, `CascadeType.ALL` means save/update/delete operations on
   the parent are applied to its children.
7. **What is the difference between first-level and second-level cache?** – The first-level cache is the Hibernate
   Session cache (one per session); it always stores loaded entities within a transaction. The second-level cache is
   optional, shared across sessions (configured per `SessionFactory`), and caches entities between transactions.
8. **What is lazy loading vs eager loading?** – Lazy loading (`FetchType.LAZY`) defers loading of associations until
   they are accessed. Eager loading (`FetchType.EAGER`) fetches associations immediately with the parent entity. LAZY is
   default for collections; EAGER for many-to-one/one-to-one (in JPA defaults).
9. **What is HQL and how does it differ from SQL?** – HQL (Hibernate Query Language) is an object-oriented query
   language that uses entity names and properties, not table names. Hibernate translates HQL into the database’s SQL. It
   supports inheritance and associations.
10. **How do you define and use a NamedQuery?** – A named query is a statically defined query with a name, created with
    `@NamedQuery` or in XML. It’s validated at startup and can be reused. For example:

    ```java
    @NamedQuery(name="Emp.findAll", query="FROM Employee")
    ```

    Then use `session.createNamedQuery("Emp.findAll", Employee.class)`. Named queries centralize query definitions and
    fail fast if they have errors.
11. **How do you map relationships (e.g. one-to-many)?** – Use JPA annotations: `@OneToMany` on the parent with
    `mappedBy` to the child’s `@ManyToOne`. Example:

    ```java
    public class Customer {
        @OneToMany(mappedBy="customer")
        private Set<Order> orders;
    }
    public class Order {
        @ManyToOne
        @JoinColumn(name="customer_id")
        private Customer customer;
    }
    ```

    This creates a foreign key `customer_id` in the `Order` table.
12. **What is the use of `hibernate.hbm2ddl.auto` in Spring Boot?** – It controls automatic schema generation. For
    example, `spring.jpa.hibernate.ddl-auto=update` tells Hibernate to update the schema to match entities. Spring Boot
    defaults to `create-drop` for an in-memory DB and `none` for others. Use `none` or `validate` in production.
13. **How is transaction management done in Hibernate?** – Manually, you call `session.beginTransaction()`, then
    `tx.commit()` or `tx.rollback()`. Spring simplifies this with `@Transactional`, which automatically
    begins/commits/rolls back Hibernate transactions behind the scenes.
14. **What annotations or configurations are needed for Hibernate in Spring Boot?** – Typically just include the
    `spring-boot-starter-data-jpa` dependency. Annotate entities with `@Entity`, repositories with Spring Data
    interfaces. Configure the database via `spring.datasource.*` and Hibernate via `spring.jpa.hibernate.*` properties.
    Spring Boot auto-configures the `SessionFactory`/`EntityManager`.
15. **How can you execute a native SQL query?** – Use `session.createNativeQuery()` or
    `EntityManager.createNativeQuery()`. You can map results to entities or scalar values. Alternatively, define a
    `@NamedNativeQuery` with SQL. This is useful for vendor-specific or complex queries, similar to HQL but written in
    the database’s SQL dialect.

