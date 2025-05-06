package Servlets.session.management;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

import java.io.IOException;

@WebServlet("/firstRequest")
public class firstRequestServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET request
        String username="Ashish";
        String userId="12345";

        Cookie cookie=new Cookie("username",username);
        Cookie cookie1=new Cookie("userId",userId);

        // Set the maximum age of the cookie to 24 hours
        cookie.setMaxAge(24*60*60);
        cookie1.setMaxAge(24*60*60);

        response.addCookie(cookie);
        response.addCookie(cookie1);


//        Http Seession management
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        session.setAttribute("userId", userId);
        session.setMaxInactiveInterval(10*60); // 10 minutes

        response.setContentType("text/html");
        response.getWriter().println("<h1>Session and Cookie Management</h1>");
    }
}
