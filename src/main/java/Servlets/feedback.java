package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/feedback")
public class feedback extends HttpServlet {

   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email").trim();
        String mobile = req.getParameter("mobile").trim();
        String feedback = req.getParameter("feedback").trim();

        // Set attributes to be accessed in the JSP
        req.setAttribute("email", email);
        req.setAttribute("mobile", mobile);
        req.setAttribute("feedback", feedback);

        // Forward to success.jsp
        req.getRequestDispatcher("components/success.jsp").forward(req, resp);
    }
}
