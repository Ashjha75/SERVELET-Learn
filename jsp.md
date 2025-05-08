# JavaServer Pages (JSP) – Interview Notes

## JSP Architecture
JavaServer Pages (JSP) is a technology for creating dynamic web content using HTML and Java. JSPs are compiled into servlets by the JSP container, allowing Java code to be embedded in HTML. The architecture consists of:
* **JSP Page:** The actual JSP file (e.g. `index.jsp`) containing HTML and Java code.
* **JSP Container:** Part of the web server (like Apache Tomcat) that processes JSP files. It translates JSPs into servlets, compiles them, and manages their lifecycle.
* **Servlet:** The Java class generated from the JSP. It contains methods to handle requests and responses.
* **Web Server:** The server (like Tomcat) that hosts the JSP container and serves HTTP requests.
* **Client:** The web browser or application that sends requests to the server and receives responses.
* **Request/Response Cycle:** The client

```
+------------+        1. Request JSP Page        +-----------------------------+
|  Browser   |  ------------------------------>  | Web Server (e.g., Tomcat)   |
| (Client)   |                                   | with Servlet Container       |
+------------+                                   +-----------------------------+
                                                        |
                                                        | 2. If first request or JSP modified,
                                                        |    container translates JSP to Servlet
                                                        |    (otherwise uses compiled version)
                                                        v
                                                  +-----------------+
                                                  | Generated JSP   |
                                                  | Servlet Class   |
                                                  +-----------------+
                                                        |
                                                        | 3. Servlet executes:
                                                        |    - Processes scriptlets
                                                        |    - Calls JavaBeans
                                                        |    - Generates HTML
                                                        v
                                                 +---------------------+
                                                 |  Business Logic     |
                                                 |  (JavaBeans, EJBs,  |
                                                 |   other Java code)  |
                                                 +---------------------+
                                                        |
                                                        | 4. HTML response generated
                                                        v
+------------+        <------------------------------  +-----------------------------+
|  Browser   |        5. Return final HTML page        | Web Server (Tomcat)         |
+------------+                                        +-----------------------------+
```

## JSP Lifecycle

* **Translation & Compilation:** When a JSP is first requested (or modified), the container translates the JSP into a servlet source and compiles it into a `.class`. Static content becomes `out.write()` calls, JSP elements (scriptlets, directives, tag calls, etc.) are converted into Java code.
* **Loading & Initialization:** The servlet class is loaded by the container, an instance is created, and its `jspInit()` method is called (once). This is similar to a servlet’s `init()` and is used for one-time setup (e.g. opening DB connections).
* **Request Handling (`_jspService`):** For each request, the container invokes the `_jspService(request, response)` method of the JSP servlet. This method is auto-generated and contains the code for the JSP’s body, writing output back to the client. It handles all incoming HTTP methods (GET, POST, etc.) and is called repeatedly.
* **Destroy/Cleanup:** When the JSP is to be removed (server shutdown or reload), the container calls `jspDestroy()`, allowing any cleanup (closing resources).

## JSP Scripting Elements

JSP supports three scripting elements to embed Java:

* **Scriptlet (`<% ... %>`):** Arbitrary Java code. Example:

  ```jsp
  <% int a = 5; %>
  <% out.println("Hello, user!"); %>
  ```
* **Declaration (`<%! ... %>`):** Declare variables or methods at the servlet class level. Example: `<%! private int counter = 0; %>` (creates `int counter` as an instance variable).
* **Expression (`<%= ... %>`):** Evaluate and output an expression’s value as a string. No semicolon needed. Example: `<p>Current time: <%= new java.util.Date() %></p>` prints the date.

Using scriptlets mixes Java and HTML, which is discouraged in modern applications (prefer JSTL/EL). For clarity, a snippet:

```jsp
<html>
  <body>
    <%-- Scriptlet example --%>
    <%
      int count = 5;
      out.println("Count is " + count);
    %>
    <%-- Declaration example --%>
    <%! int initial = 10; %>
    <%-- Expression example --%>
    <p>Sum: <%= count + initial %></p>
  </body>
</html>
```



## JSP Directives

Directives give instructions to the JSP container at **translation time**:

* **`<%@ page ... %>`:** Page-level settings (usually placed at top). Common attributes include: `language` (e.g. `"java"`), `contentType`, `import` (Java packages), `buffer`, `errorPage`, `isErrorPage`, `session` (true/false), etc. For example, `<%@ page contentType="text/html; charset=UTF-8" import="java.util.List" %>`. The `errorPage` attribute names a JSP to handle uncaught exceptions, and `isErrorPage="true"` is set in that error JSP to enable use of the `exception` object.
* **`<%@ include file="relativePath" %>` (Include Directive):** Statically includes another file at translation time. The included file’s content is merged into the current JSP before compilation. Syntax example: `<%@ include file="header.jsp" %>`. Use this for reusable static content (e.g. headers, footers).
* **`<%@ taglib uri="uri" prefix="pfx" %>` (Taglib Directive):** Declares a custom tag library (including JSTL). The `uri` locates the tag library descriptor (TLD) or uses a schema URI, and `prefix` is the XML prefix to use. Example: `<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>`.

## JSP Implicit Objects

JSP provides nine built-in objects (no declaration needed) to access common servlet/JSP features. They include:

* `request` – the `HttpServletRequest` for this request (retrieve parameters, headers).
* `response` – the `HttpServletResponse` for sending data (e.g. `response.setContentType`).
* `out` – a `JspWriter` (similar to `PrintWriter`) to write output.
* `session` – the `HttpSession` associated with the user (if any).
* `application` – the `ServletContext` for the web app (application scope).
* `config` – the `ServletConfig` for this JSP’s servlet.
* `pageContext` – a JSP `PageContext` object giving access to page-scoped attributes and more.
* `page` – a reference to the servlet instance (like `this`).
* `exception` – the `Throwable` that caused the error, available only in an error page (`isErrorPage="true"`).

These objects let JSPs read request params (`request.getParameter`), session data, write to the output (`out`), etc., without explicit lookup.

## Expression Language (EL)

EL (introduced in JSP 2.0) simplifies accessing data in JSPs. Instead of scriptlets, you can write `${expression}`. EL can reference:

* **Scoped attributes:** e.g. `${user.name}`, where `user` is a JavaBean or attribute in request/session/application scope.
* **Implicit maps:** `${param.firstName}` for request parameter “firstName”; `${sessionScope.cart.items}` for a session attribute; `${header["User-Agent"]}` for request header; and so on. (Default search order: page, request, session, application).
* **Operators & functions:** EL supports arithmetic, logical, and relational operators inside `${ }`.

Example:

```jsp
<p>Logged-in as: ${sessionScope.user.username}</p>
<p>Number of items: ${empty cart.items ? 0 : cart.items.size()}</p>
```

This avoids Java code in JSP. By default `${ }` is evaluated; to turn off EL (rarely needed), use `isELIgnored="true"` in `<%@ page %>`.



## JSTL Core Tags (Overview)

The JSTL Core library (taglib URI `http://java.sun.com/jsp/jstl/core`, usually prefix `c`) provides common functionality in JSP without scriptlets. Key core tags include:

* **Output:** `<c:out value="${expr}" />` – prints escaped output, like `<%= ... %>` safely.
* **Variables:** `<c:set var="name" value="${expr}"/>` and `<c:remove var="name"/>` (set or remove page/request/session variables).
* **Flow Control:** `<c:if test="${cond}">…</c:if>`, and `<c:choose>` with `<c:when test="${…}">` / `<c:otherwise>` for if-then-else.
* **Iteration:** `<c:forEach var="item" items="${collection}" begin="..." end="..." step="...">` (loop over lists/ranges), and `<c:forTokens>` for tokenizing a String.
* **URL/Import:** `<c:url>` (to build context-relative URLs), `<c:param>` (add query parameters), `<c:redirect>` (send redirect), `<c:import url="..." />` (include content from URL).
* **Exception:** `<c:catch var="ex">…</c:catch>` – catches any `Throwable` in its body.

These JSP-friendly tags (all prefix `c`) cover output, logic, loops, URL handling, etc. and help keep JSPs scriptless.

## MVC with Servlet + JSP

In a typical MVC pattern using Servlets and JSP, **Servlets act as controllers** and **JSPs act as views**. The controller servlet processes incoming requests (often using model/beans or DAOs for data), then stores results in the request/session scope and forwards to a JSP for rendering. The JSP uses those attributes and EL/JSTL to display the result. This separation (Servlet handles logic, JSP handles presentation) leads to cleaner, maintainable code. *Example:* A `LoginServlet` checks user credentials (model), then does `request.setAttribute("user", userObj); request.getRequestDispatcher("home.jsp").forward(request,response);`. The `home.jsp` view then shows `${user.name}` on the page. (Avoid putting business logic in JSP.)

## Forwarding Data from Servlet to JSP

To pass data from a servlet to a JSP:

* In the **Servlet**, use `request.setAttribute("key", value)` to attach any objects or primitives you need.
* Obtain a `RequestDispatcher` for the target JSP and call `forward(request, response)`. The client’s URL stays the same.
* In the **JSP**, access those attributes via EL or implicit objects (e.g. `${key}` or `<%= request.getAttribute("key") %>`).

For example, in a servlet:

```java
// In Servlet doGet()/doPost()
String name = "John";
request.setAttribute("name", name);
RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
rd.forward(request, response);
```

And in `home.jsp`:

```jsp
<p>Welcome, ${name}!</p>
```

This outputs “Welcome, John!” without exposing SQL/logic in the JSP.

## Form Handling with JSP

JSP can read HTML form data sent via GET or POST. Use the **`request.getParameter(name)`** methods (or implicit EL `${param.name}`) to retrieve values. For example, given a form:

```html
<form method="post" action="submit.jsp">
  First Name: <input type="text" name="firstName"/>
  <input type="submit" value="Send"/>
</form>
```

In `submit.jsp`, you can do:

```jsp
<p>First Name: ${param.firstName}</p>
```

or using scriptlet: `<%= request.getParameter("firstName") %>`. Use `getParameterValues("name")` when a parameter has multiple values (e.g. checkboxes). No extra JSON or parsing is needed – JSP containers handle the parsing of form data.



## JSP Exception Handling

To handle runtime exceptions in JSP, use the `page` directive’s `errorPage` and `isErrorPage` attributes. In your JSP (e.g. `main.jsp`), add:

```jsp
<%@ page errorPage="error.jsp" %>
```

This tells the container to forward any uncaught exception to `error.jsp`. In `error.jsp`, set:

```jsp
<%@ page isErrorPage="true" %>
```

This makes the implicit `exception` object (a `Throwable`) available. You can then display `exception.getMessage()` or other details. In `error.jsp`, reference details like:

```jsp
<p>Error: <%= exception.getMessage() %></p>
```

This mechanism avoids writing try/catch in JSPs. The `errorPage` attribute is for the main page, and `isErrorPage` must be true in the target page.

## Include Mechanisms (Static vs Dynamic)

JSP offers two ways to include content:

* **Static Include (`<%@ include file="..." %>`):** As mentioned, it merges the included file at translation time. The included content (JSP or HTML) is compiled as part of the current page’s servlet. Use for static fragments. Example: `<%@ include file="header.jsp" %>`.
* **Dynamic Include (`<jsp:include page="..." />` action):** This tag is processed at runtime. The included resource (JSP/servlet/HTML) is invoked each time and its output is inserted. Example: `<jsp:include page="footer.jsp" flush="true"/>`. If including a static page, its content is inserted; if including a JSP/servlet, that resource executes and returns output. Use dynamic include for content that may change per request.

Example snippet:

```jsp
<%@ include file="navbar.jsp" %>        <!-- static include, happens at compile-time -->
...
<jsp:include page="dynamicContent.jsp" />  <!-- dynamic include, happens at runtime -->
```



## Custom Tags (Basic Idea)

Custom tags are user-defined JSP tags (like new HTML tags) backed by Java **tag handler** classes. They help encapsulate presentation logic and avoid scriptlets. To create one, you need: a tag handler (a Java class, either a *simple tag* or *classic tag handler*), a Tag Library Descriptor (TLD) XML file defining the tag, and a `<%@ taglib %>` directive to use it. In JSP you’d write something like `<mylib:helloWorld />`. Under the covers, the container instantiates your tag handler and runs its `doTag()` or `doStartTag()/doEndTag()` logic. Custom tags increase readability and reusability of JSP code. (For example, JSTL’s `<c:forEach>` is a custom tag defined by the JSTL tag library.)



## Interview Questions

* **What is JSP and how does it differ from Servlets?** (Talk about JSP being a higher-level view technology, auto-compiling to servlets, easier HTML embedding.)
* **Explain the JSP lifecycle (translation, compilation, jspInit, \_jspService, jspDestroy).**
* **Name the JSP scripting elements and their syntax.** (Scriptlet `<% %>`, Declaration `<%! %>`, Expression `<%= %>`.)
* **What are JSP directives?** (Explain `<%@ page %>`, `<%@ include %>`, `<%@ taglib %>` and give examples/attributes.)
* **How do you include content in JSP?** (Difference between `<%@ include file="...">` and `<jsp:include page="..."/>`.)
* **List the JSP implicit objects and briefly say what each is for.** (e.g. `request`, `response`, `session`, `application`, `out`, etc.)
* **What is JSP EL and why use it?** (Syntax `${}`, simplifies data access, avoids scriptlets.)
* **What is JSTL? Give examples of some JSTL core tags.** (Mention `<c:forEach>`, `<c:if>`, `<c:out>`, `<c:set>`, `<c:choose>`, etc.)
* **How do you forward data from a Servlet to a JSP?** (Use `request.setAttribute()` then `RequestDispatcher.forward()` and access with `${}` in JSP.)
* **How do you retrieve form parameters in JSP?** (Use `request.getParameter("name")` or `${param.name}`.)
* **How do you handle exceptions in JSP?** (Use `errorPage` and `isErrorPage` with an error JSP.)
* **What is a JSP custom tag?** (Describe purpose of tag handler, TLD, `<%@ taglib %>`.)
* **Explain how JSP fits into the MVC pattern.** (Servlet = Controller, JSP = View, model = beans/DAOs.)
* **What’s the difference between `sendRedirect` and `forward` in JSP/Servlet context?** (Though more servlet-related, often asked with JSP.)
* **What is the purpose of the `jspInit()` and `jspDestroy()` methods?** (JSP-specific init/destroy methods analogous to servlet init/destroy.)
