[servlet.html](:/7192741b1aae450c874e743fe5dd590a)

* * *

# Introduction to Servlets

1.  Servlets are Java programs that run on a web server, handling client requests and generating dynamic responses.

2.  Servlets are used to process or store data submitted by an HTML form, provide dynamic content like returning results from a database query, manage state information, etc.


* * *

# Servlet Lifecycle

The lifecycle of a servlet is controlled by the **container** (e.g., Tomcat), and it goes through the following stages:

1.  **Loading and Instantiation**  
    The servlet class is loaded and an instance is created.

2.  **Initialization (`init` method)**  
    The `init` method is called once to initialize the servlet.

3.  **Request Handling (`service` method)**  
    The `service` method is called for each request to process it.

4.  **Destruction (`destroy` method)**  
    The `destroy` method is called once before the servlet is removed from service.


* * *

## Setting Up the Environment

### Required Tools

- **JDK**: Java Development Kit

- **Apache Tomcat**: Web server and servlet container

- **Eclipse/IntelliJ IDEA**: Integrated Development Environment (IDE)

- Configure Tomcat with IDE


* * *

## Creating a Simple Project

### Eclipse Setup

1.  Open Eclipse and create a new **Dynamic Web Project**.

2.  Set up Apache Tomcat in Eclipse.

3.  Create a new servlet class.


* * *

# Creating First Servlet

There are three ways to create a servlet:

1.  Implementing the **Servlet** Interface

2.  Extending the **GenericServlet** class

3.  Extending the **HttpServlet** class


### Example

```java
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Hello, World!</h1>");
    }
}
```

> Run the project; the IDE will automatically deploy the servlet to the web server.

* * *

# Handling Requests and Responses

Servlets handle HTTP requests from clients (browsers) and generate dynamic responses.

### 1\. Client Sends Request

A client (browser) sends an HTTP request to the web server containing:

- **Request Method** (GET, POST, etc.)

- **URL** of the requested resource (Servlet)

- **Headers** (e.g., user agent, cookies)

- **Optional request body** (for POST requests)


### 2\. Servlet Container Intercepts

- The web server's Servlet container intercepts the request.

- It identifies the corresponding servlet based on the URL mapping defined in `web.xml`.


### 3\. Servlet Invoked (`doGet`/`doPost`)

- The container invokes the appropriate method:

    - `doGet(HttpServletRequest, HttpServletResponse)`

    - `doPost(HttpServletRequest, HttpServletResponse)`

    - Others for PUT, DELETE, etc.


### 4\. Processing Request

Use the `request` object:

- `getParameter(String name)` for form/query parameters

- `getHeader(String name)` for headers

- `getInputStream()` or `BufferedReader` for request body (POST)


### 5\. Generating Response

Use the `response` object:

- `setContentType(String type)`

- `getWriter()` or `getOutputStream()`

- `setStatus(int code)`


### 6\. Sending Response

- The Servlet container sends the response back to the client.

* * *

# Creating a Feedback Form Application with Servlets

## 1\. Define the Feedback Form (HTML)

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Feedback Form</title>
</head>
<body>
  <h1>Feedback Form</h1>
  <form action="submitFeedback" method="post">
    <label for="name">Name (Optional):</label>
    <input type="text" id="name" name="name"><br>
    <label for="feedback">Feedback:</label>
    <textarea id="feedback" name="feedback" rows="5" cols="30"></textarea><br>
    <button type="submit">Submit Feedback</button>
  </form>
</body>
</html>
```

## 2\. Create a Feedback Servlet (Java)

```java
public class FeedbackServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String name = request.getParameter("name");  // Optional
    String feedback = request.getParameter("feedback");

    // Process the feedback (e.g., store in database, send email)
    System.out.println("Received feedback from: " + name);
    System.out.println("Feedback message: " + feedback);

    // (Optional) Send a success response
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h1>Thank you for your feedback!</h1>");
  }
}
```

## 3\. Web.xml Configuration

```xml
<web-app>
  <servlet>
    <servlet-name>FeedbackServlet</servlet-name>
    <servlet-class>com.yourpackage.FeedbackServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>FeedbackServlet</servlet-name>
    <url-pattern>/submitFeedback</url-pattern>
  </servlet-mapping>
</web-app>
```

* * *

# Session Management

Maintaining user state across multiple requests.

## 1\. Using HttpSession

```java
public class LoginServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    // (Assuming successful login logic)
    HttpSession session = request.getSession();
    session.setAttribute("loggedInUser", username);

    response.sendRedirect("welcome.jsp");
  }
}

public class WelcomeServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String username = (String) session.getAttribute("loggedInUser");

    if (username != null) {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("<h1>Welcome, " + username + "!</h1>");
    } else {
      response.sendRedirect("login.html");
    }
  }
}
```

## 2\. Using Cookies

```java
public class RememberMeServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String rememberMe = request.getParameter("rememberMe");

    if (rememberMe != null && rememberMe.equals("on")) {
      String userId = "12345"; // Sample ID
      Cookie cookie = new Cookie("userId", userId);
      cookie.setMaxAge(60 * 60 * 24 * 7); // One week
      response.addCookie(cookie);
    }

    response.sendRedirect("index.jsp");
  }
}
```

## 3\. URL Rewriting (Not Recommended)

```java
String sessionId = request.getSession().getId();
String targetUrl = "welcome.jsp?sessionid=" + sessionId;
response.sendRedirect(targetUrl);
```

* * *

# Error Handling

Define custom error pages in `web.xml`:

```xml
<error-page>
    <error-code>404</error-code>
    <location>/error404.html</location>
</error-page>
```

* * *

# ServletContext and ServletConfig

## ServletContext

- **Definition**: Interface for communication between servlet and container.

- **Scope**: Shared across the whole web application.

- **Purpose**: For sharing global parameters and resources.


### Key Methods

- `getInitParameter(String name)`

- `getAttribute(String name)`

- `setAttribute(String name, Object object)`

- `getRealPath(String path)`


```java
public class ContextExampleServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        String paramValue = context.getInitParameter("globalParam");
        response.getWriter().println("Global Param: " + paramValue);

        context.setAttribute("sharedData", "This is shared across the application");
        String sharedData = (String) context.getAttribute("sharedData");
        response.getWriter().println("Shared Data: " + sharedData);
    }
}
```

## ServletConfig

- **Definition**: Provides servlet-specific config info.

- **Scope**: Per servlet.

- **Purpose**: For servlet-specific initialization parameters.


### Key Methods

- `getServletName()`

- `getInitParameter(String name)`

- `getServletContext()`


```java
public class ConfigExampleServlet extends HttpServlet {
    private String initParam;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initParam = config.getInitParameter("initParam");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.getWriter().println("Init Param: " + initParam);
    }
}
```

### XML Configuration

```xml
<servlet>
    <servlet-name>ConfigExampleServlet</servlet-name>
    <servlet-class>com.example.ConfigExampleServlet</servlet-class>
    <init-param>
        <param-name>initParam</param-name>
        <param-value>This is an init parameter</param-value>
    </init-param>
</servlet>
<servlet-mapping>
    <servlet-name>ConfigExampleServlet</servlet-name>
    <url-pattern>/configExample</url-pattern>
</servlet-mapping>
```

* * *
# NEW SERVLET things

Great! I’ll prepare a concise, step-by-step markdown (`.md`) note that focuses only on the essential Servlet concepts you need to understand Spring Boot and prepare for interviews. This will include:

* Key servlet fundamentals (Servlet lifecycle, HttpServlet, etc.)
* Servlet configuration (via web.xml and annotations)
* Key concepts like request/response objects, filters, listeners
* Differences between servlet and Spring MVC (only for context)
* Common interview questions and answers

I’ll keep the focus strictly on what’s relevant for Spring Boot and interviews—nothing extra. I’ll let you know once it’s ready.


# What is a Servlet?

A **Servlet** is a Java class that runs in a web server (servlet container) to handle client requests using a request–response model. It extends a server’s capabilities by generating dynamic content (e.g. HTML, JSON) on the server side. Key points:

* **Runs on server:** Executes within a Java-enabled web server or app server, managed by a servlet container (e.g. Tomcat).
* **Request–response:** Processes `HttpServletRequest` (from client) and produces `HttpServletResponse` (back to client).
* **Dynamic content:** Generates content at runtime (unlike static HTML).

# Servlet Lifecycle

The servlet lifecycle has four main stages (often shown in a diagram):

1. **Loading/Instantiation:** The container loads the servlet class and creates one instance (either at startup or on first request).
2. **init():** Called once after instantiation. Initialize resources here (e.g. database connections). This happens only once per servlet instance.
3. **service():** Invoked for each client request. A new thread is spawned per request, and the container calls `service(request,response)`. The default `service()` implementation dispatches to `doGet()`, `doPost()`, etc., based on the HTTP method.
4. **destroy():** Called once when the servlet is being taken out of service (e.g. server shutdown). Perform cleanup here (close connections, stop threads).

# HttpServlet and doGet/doPost

* **HttpServlet:** A servlet typically extends `javax.servlet.http.HttpServlet` (a subclass of `GenericServlet`). You should extend `HttpServlet` and override methods to handle requests.
* **doGet / doPost:** Override `doGet(HttpServletRequest, HttpServletResponse)` to handle HTTP GET requests (e.g. page loads) and `doPost(...)` for POST requests (e.g. form submissions). The container’s default `service()` method automatically routes to `doGet()` or `doPost()` (and others) based on the request type. For example:

  ```java
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException {
      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      out.println("<h1>Hello, world!</h1>");
  }
  ```

  (Use `doPost()` similarly for POST; override whichever methods your servlet needs.)

# Servlet Configuration

Servlets can be mapped to URL patterns either via **web.xml** (deployment descriptor) or via annotations:

* **web.xml:** Define a `<servlet>` and `<servlet-mapping>` entry. For example:

  ```xml
  <servlet>
    <servlet-name>HelloServlet</servlet-name>
    <servlet-class>com.example.HelloServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>
  ```
* **@WebServlet:** (Servlet 3.0+) Annotate the servlet class to map URLs, eliminating the need for web.xml. Example:

  ```java
  @WebServlet("/hello")
  public class HelloServlet extends HttpServlet {
      // ...
  }
  ```

Annotations make configuration more concise; web.xml is still used in some legacy setups or for complex mappings.

# Request and Response Objects

Each servlet request is encapsulated in an `HttpServletRequest` and response in an `HttpServletResponse`. They provide many methods to access request data and control the response:

* **HttpServletRequest:** Contains client request data. Key methods include `getParameter("name")` (form/query data), `getHeader("User-Agent")` (headers), `getCookies()`, `getSession()` (to retrieve or create an `HttpSession`), `getInputStream()` or `getReader()` (for request body), and `getAttribute()` (for request-scoped data set by filters or servlets).
* **HttpServletResponse:** Used to set up the response. Key methods include `setContentType("text/html")` or other MIME types, `getWriter()` or `getOutputStream()` to write response content, `sendRedirect("/url")` to redirect the client, `setStatus(code)` to set HTTP status, and `addCookie(Cookie)` to send cookies.

# ServletContext and ServletConfig

* **ServletConfig:** One per servlet instance. Holds init parameters for that specific servlet (from `<init-param>` in web.xml) and servlet name. Access it via `getServletConfig()` or `config.getInitParameter("paramName")`. Use `ServletConfig` for servlet-specific settings.
* **ServletContext:** One per web application. A global context for all servlets. Holds context-wide init parameters (`<context-param>` in web.xml) and attributes. Access via `getServletContext()`. Data put in `ServletContext.setAttribute(name, value)` is shared across the whole app. In short, *ServletConfig* is servlet-specific, while *ServletContext* is application-wide.

# Filters and Listeners

* **Filters:** Objects that implement `javax.servlet.Filter`. They are invoked before and/or after servlet processing to perform tasks like logging, authentication, encoding, or compression. In `doFilter(request, response, chain)`, you can examine or modify the request/response, then call `chain.doFilter(request, response)` to continue the chain. Filters are declared in web.xml or via `@WebFilter("/*")`. Example use cases include checking user credentials or compressing the response.
* **Listeners:** Objects that implement listener interfaces to respond to servlet context, session, or request events. For example, a `ServletContextListener` has `contextInitialized()` (called at application startup) and `contextDestroyed()` (at shutdown). Other examples include `HttpSessionListener` (notified on session create/destroy) and `ServletRequestListener`. Listeners are declared in web.xml or via `@WebListener`. They allow running code on lifecycle events (e.g. initialize resources on startup, clean up on shutdown).

# Thread Safety and Servlet

Servlets are **multithreaded** by default: the container creates one instance of each servlet and uses a new thread for each incoming request. This means:

* **One instance, many threads:** Do *not* use instance fields to store per-request data, since multiple threads will access the same servlet object concurrently. Always use local variables inside `doGet/doPost` for request-specific data.
* **SingleThreadModel (deprecated):** Servlet API once offered a `SingleThreadModel` marker to force one thread at a time, but it’s deprecated and insufficient (static fields and sessions can still be unsafe). Instead, write servlets to be stateless or synchronize explicitly if sharing mutable state.

# Comparison to Spring MVC

Spring MVC is a higher-level framework built on the Servlet API. In Spring MVC:

* All requests go through a front controller (`DispatcherServlet`, itself a servlet), which dispatches to handler methods in `@Controller` classes.
* You define routes with annotations (e.g. `@RequestMapping`) and return view names or data; Spring handles binding request parameters, formatting responses, view resolution, etc.
* In contrast, raw servlets require manually parsing `HttpServletRequest` parameters, writing to `HttpServletResponse`, and managing views. Spring MVC “makes building web apps easier” by taking care of these details, while still using servlets under the hood.

# Common Interview Questions

* **What is a Java Servlet?** A Java class running in a web container that handles HTTP requests and generates dynamic responses.
* **Describe the servlet lifecycle.** The key methods are `init()` (called once to initialize), `service()` (called per request, dispatching to `doGet()`, `doPost()`, etc.), and `destroy()` (called once at teardown).
* **Difference between doGet and doPost?** `doGet()` handles HTTP GET (safe, idempotent requests), while `doPost()` handles POST (e.g. form submissions). The servlet’s `service()` method routes to one or the other based on the request. Override whichever you need; if only one method is overridden, the other will return HTTP 405 by default.
* **How do you configure a servlet?** Either in *web.xml* with `<servlet>` and `<servlet-mapping>` (specifying servlet name, class, and URL pattern), or by annotating the servlet class with `@WebServlet("/urlPattern")` (no XML needed).
* **ServletConfig vs. ServletContext?** `ServletConfig` holds config for one servlet (init-params from web.xml for that servlet). `ServletContext` is application-wide (shared by all servlets). Init parameters for the whole app come from `<context-param>` in web.xml and are accessed via `getServletContext().getInitParameter()`.
* **What is a Filter?** A filter is a component that wraps servlet execution for preprocessing or postprocessing of requests/responses. Filters implement `Filter.doFilter()`, where you can examine/modify the request or response and then continue the chain. They are commonly used for tasks like logging, authentication, or encoding.
* **What is a Listener?** A listener is a component that listens for servlet lifecycle events. For example, a `ServletContextListener` can run code on application startup (`contextInitialized()`) or shutdown (`contextDestroyed()`). Other listeners include `HttpSessionListener` (session creation/destruction) and `ServletRequestListener` (request creation/destruction).
* **How are servlets threaded?** The container typically creates one servlet instance and spawns a new thread for each request. Thus servlets must be written to be thread-safe: don’t use instance variables for request-specific data. (The `SingleThreadModel` interface once limited concurrency, but it’s deprecated.)
* **Forward vs. Redirect?** Using `RequestDispatcher.forward(request,response)` forwards control internally on the server (URL stays the same, and request attributes are preserved). `response.sendRedirect(url)` sends a 302 redirect to the client’s browser, causing a new request to the given URL (the browser’s address bar changes). Use forward to pass control within the app, and redirect to instruct the client to fetch a new URL.
* **Context-param vs. init-param?** `<context-param>` in web.xml defines parameters for the entire application (accessible via `ServletContext.getInitParameter()`). `<init-param>` inside a `<servlet>` defines parameters for that specific servlet (accessible via `ServletConfig.getInitParameter()`).
* **How to share data between servlets?** You can share data via request attributes (for one request/forward), session attributes (one user’s session via `request.getSession().setAttribute()`), or context attributes (app-wide via `getServletContext().setAttribute()`). Use the appropriate scope (request/session/application) based on the use case.
* **Advantages of servlets over CGI?** Servlets run in a Java VM and reuse threads, so they are much faster and more scalable than CGI scripts. Unlike CGI (which spawns a new process per request), a single servlet instance can handle many requests in separate threads. Benefits include faster execution, portability (Java’s “write once, run anywhere”), single-instance handling of multiple requests, and easy integration with Java APIs (JDBC, etc.).

**Sources:** Core servlet concepts from Oracle Java EE tutorials and GeeksforGeeks, among others.

