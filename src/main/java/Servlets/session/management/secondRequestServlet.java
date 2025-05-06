package Servlets.session.management;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;

@WebServlet("/secondRequest")
public class secondRequestServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle GET request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    String username = cookie.getValue();
                    System.out.println("Username: " + username);
                }
                if (cookie.getName().equals("userId")) {
                    String userId = cookie.getValue();
                    System.out.println("User ID: " + userId);
                }
            }
        }

        // Retrieve session attributes
        String username = (String) request.getSession().getAttribute("username");
        String userId = (String) request.getSession().getAttribute("userId");
        if (username != null && userId != null) {
            System.out.println("Session Username: " + username);
            System.out.println("Session User ID: " + userId);
        } else {
            System.out.println("No session attributes found.");
        }
    }


}
