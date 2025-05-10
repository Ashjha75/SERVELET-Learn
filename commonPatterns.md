* Core OOP concepts (Encapsulation, Abstraction, Inheritance, Polymorphism)
* SOLID principles
* Additional important principles (GRASP, DRY, KISS, YAGNI, Law of Demeter, Composition Over Inheritance, etc.)
  Each topic will include a clear explanation and a Java code example, with a balance between depth and brevity to suit interview prep needs.
  I’ll let you know as soon as the notes are ready for review.


# Core OOP Concepts

* **Encapsulation:** Binding data and methods into one unit, hiding internal state. In Java this means marking fields `private` and exposing only getters/setters. For example:

  ```java
  class Person {
      private String name;
      public String getName() { return name; }
      public void setName(String name) { this.name = name; }
  }
  ```

  This hides the `name` field from external access while allowing controlled updates.

* **Abstraction:** Showing only essential information and hiding details. In Java this is achieved via abstract classes or interfaces. For example:

  ```java
  abstract class Animal {
      abstract void makeSound();
  }
  class Dog extends Animal {
      void makeSound() { System.out.println("Woof!"); }
  }
  ```

  Here `Animal` hides the implementation of `makeSound()`, exposing only the interface.

* **Inheritance:** A way to form new classes using properties of existing ones. The derived class (subclass) reuses or extends the behavior of the base (super) class. For example:

  ```java
  class Vehicle { void start() { /* common start code */ } }
  class Car extends Vehicle {
      void openTrunk() { /* Car-specific code */ }
  }
  ```

  `Car` inherits `start()` from `Vehicle`, promoting code reuse.

* **Polymorphism:** “Many forms” – objects can be treated as instances of their parent type, with behavior varying by actual subclass. In Java this happens via method overriding or overloading. For example:

  ```java
  class Animal { void sound() { System.out.println("Some sound"); } }
  class Cat extends Animal { void sound() { System.out.println("Meow"); } }
  class Dog extends Animal { void sound() { System.out.println("Woof"); } }
  Animal a = new Cat(); 
  a.sound();  // Prints "Meow"
  ```

  Here `a.sound()` calls the `Cat` version of `sound()`, not the base version.

# SOLID Principles

* **Single Responsibility Principle (SRP):** A class should have one, and only one, reason to change. Each class/module should handle one specific task.
  *Example:* Instead of one class doing file I/O and validation, split it:

  ```java
  class UserRepository {
      void save(User u) { /* save to DB */ }
  }
  class EmailService {
      void sendEmail(User u) { /* send email */ }
  }
  ```

  Each class has a single responsibility, making maintenance and testing easier.

* **Open/Closed Principle (OCP):** Software entities (classes, modules, functions) should be *open for extension* but *closed for modification*. You should be able to add new behavior without changing existing code. In Java this often uses interfaces or abstract classes.
  *Example:*

  ```java
  interface Shape { double area(); }
  class Rectangle implements Shape { double area() { /*...*/ } }
  class Circle implements Shape { double area() { /*...*/ } }
  // To add a new shape, just implement Shape; existing code remains unchanged.
  ```

  This lets you extend functionality (new `Shape`) without editing the existing `Shape`-using code.

* **Liskov Substitution Principle (LSP):** Subclasses must be substitutable for their base classes without altering correctness. In other words, code that uses a base class should work with any subclass. Violations occur if a subclass changes expected behavior.
  *Example Violation:*

  ```java
  class Rectangle {
      void setWidth(int w) { /*...*/ }
      void setHeight(int h) { /*...*/ }
  }
  class Square extends Rectangle {
      void setWidth(int w) { super.setWidth(w); super.setHeight(w); }
      void setHeight(int h) { setWidth(h); }
  }
  ```

  Here `Square` is not a true substitute for `Rectangle`, because setting width or height in a `Square` alters the other dimension, breaking client expectations. A better design avoids this inheritance or restricts behavior.

* **Interface Segregation Principle (ISP):** Clients should not be forced to depend on methods they do not use. Large “fat” interfaces should be split into smaller, specific ones.
  *Example:*

  ```java
  // Bad: single interface with too many methods
  interface MultiFunctionDevice {
      void print();
      void scan();
      void fax();
  }
  // Many clients only need printing, and would be forced to implement unused methods.

  // Better: segregate into focused interfaces
  interface Printer { void print(); }
  interface Scanner { void scan(); }
  ```

  Now a class can implement only the interfaces it needs, avoiding unused methods.

* **Dependency Inversion Principle (DIP):** High-level modules should not depend on low-level modules; both should depend on **abstractions** (e.g., interfaces). Also, abstractions should not depend on details. This inverts the typical direction of dependency.
  *Example:* Instead of a service class instantiating a database class directly, use an interface:

  ```java
  interface Database { void save(Data d); }
  class MySqlDatabase implements Database { 
      public void save(Data d) { /* MySQL save */ } 
  }
  class Service {
      private Database db;
      Service(Database db) { this.db = db; }
      void process(Data d) { db.save(d); }
  }
  // Service depends on the abstraction Database, not a concrete MySqlDatabase.
  ```

  This makes the code flexible: `Service` can work with any `Database` implementation, reducing coupling.

# Other Design Principles and Patterns

* **GRASP Principles (General Responsibility Assignment Software Patterns):** A set of guidelines for assigning responsibilities to classes. Key GRASP principles include *Information Expert* (assign to class with the needed information), *Creator* (which class should create objects), *Controller* (handle system events), *Low Coupling* (minimize inter-class dependencies), *High Cohesion* (keep related behaviors together), *Polymorphism* (use runtime polymorphism to handle variations), *Pure Fabrication* (create artificial classes to achieve high cohesion/low coupling), *Indirection* (use intermediate classes to decouple), and *Protected Variations* (encapsulate change points behind stable interfaces). These patterns help design clear, maintainable object-oriented architectures.

* **DRY (Don't Repeat Yourself):** Avoid duplicating code or information. Instead, abstract common code into methods or classes. For example, if two parts of code sort lists, extract a `sort(List l)` method rather than writing sort logic twice. DRY reduces errors and makes updates easier when logic must change.

* **KISS (Keep It Simple, Stupid):** Favor simple designs. Solutions should be as straightforward as possible – unnecessary complexity is avoided. For example, use clear, concise code instead of clever, convoluted logic. A simpler design is easier to understand, maintain, and less error-prone.

* **YAGNI (You Aren't Gonna Need It):** Do not add functionality until it is necessary. In practice, avoid writing code speculatively for future features. For example, don’t implement configurable logging levels before you actually need them. This keeps the codebase lean and maintainable.

* **Law of Demeter (Principle of Least Knowledge):** A class should only communicate with its close “friends” (direct collaborators), not strangers. In other words, an object should assume as little as possible about the structure of other objects. For example, avoid chained calls like `order.getCustomer().getAddress().getStreet()`. Instead, have `order.getShippingAddress()` so `Order` talks only to `Customer` (a friend), not to `Address` internals. This minimizes coupling between objects.

* **Favor Composition Over Inheritance:** Where possible, give objects functionality by including (composing) other objects, not by inheriting from them. Composition (“has-a”) leads to more flexible designs than deep inheritance (“is-a”). For instance, instead of `class SportsCar extends Car`, use `class Car { Engine engine; }`. Here `Car` has an `Engine`, so you can swap in different engine implementations without changing class hierarchies, yielding higher flexibility.

* **High Cohesion & Low Coupling:**

    * *High cohesion* means a class or module should focus on a single task or closely related tasks. Its methods and data should all relate to one purpose.
    * *Low coupling* means classes/modules should depend on each other as little as possible. Fewer and simpler dependencies make systems easier to change. High cohesion and low coupling together lead to clear, maintainable code.

* **Interface vs. Abstract Class:** Both provide abstraction, but with differences:

    * **Abstract Class:** Can have instance fields, constructors, and both abstract and concrete methods. Use it when related classes share common code or state. Example: an abstract `Vehicle` with shared fields and methods for all vehicles.
    * **Interface:** Can only (before Java 8) declare abstract methods (Java 8+ allows default methods) and `public static final` constants. No instance state. A class can implement multiple interfaces. Use interfaces to define a contract that many unrelated classes can share. For example, `Runnable` or `Comparable`.
      In summary, use an abstract class for shared implementation (single inheritance) and an interface for a pure contract or mix-in capabilities.

* **Immutable Objects:** An object whose state cannot change after construction. For an immutable class in Java: declare the class `final`, make fields `private final`, initialize all fields in the constructor, and provide no setters (only getters). Also, ensure any mutable fields are safely copied. For example:

  ```java
  public final class Person {
      private final String name;
      private final int age;
      public Person(String name, int age) {
          this.name = name;
          this.age = age;
      }
      public String getName() { return name; }
      public int getAge() { return age; }
  }
  ```

  Once created, a `Person` instance’s fields never change, making it thread-safe and simpler to reason about.

* **Builder, Singleton, Factory Patterns (briefly):**

    * **Builder Pattern:** Helps create complex objects step-by-step, especially when many optional parameters exist. It uses a nested `Builder` class with methods for setting parameters and a final `build()` method. This avoids telescoping constructors and ensures object consistency.
    * **Singleton Pattern:** Ensures a class has only one instance. Typically done with a private constructor and a `public static` `getInstance()` method. Example:

      ```java
      public class Singleton {
          private static Singleton instance;
          private Singleton() {}
          public static synchronized Singleton getInstance() {
              if (instance == null) instance = new Singleton();
              return instance;
          }
      }
      ```

      Now `Singleton.getInstance()` always returns the same object.
    * **Factory Pattern:** Provides a method for creating objects, encapsulating the instantiation logic. The client calls a factory method (often static) and gets back an instance without knowing the concrete class. This promotes coding to an interface and keeps client code decoupled from implementations. For example:

      ```java
      class ComputerFactory {
          public static Computer getComputer(String type) {
              if ("PC".equals(type)) return new PC();
              if ("Server".equals(type)) return new Server();
              return null;
          }
      }
      // Usage:
      Computer comp = ComputerFactory.getComputer("PC");
      ```

      This way, adding new `Computer` types won’t require changing client code, only the factory implementation.

Each of these concepts is commonly asked in Java interviews. Understanding and explaining them clearly—with brief code examples—will help demonstrate your design skills and depth of knowledge.

