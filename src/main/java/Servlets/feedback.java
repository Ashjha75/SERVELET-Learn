package Servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/feedback")
public class feedback extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email").trim();
        String mobile = req.getParameter("mobile").trim();
        String feedback = req.getParameter("feedback").trim();

        try (Connection conn = JDBC.HikariDBConnection.getConnection()) {
            String sql = "INSERT INTO feedbackForm (email,mobile,feedback) VALUES (?, ?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, mobile);
                stmt.setString(3, feedback);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set attributes to be accessed in the JSP
        req.setAttribute("email", email);
        req.setAttribute("mobile", mobile);
        req.setAttribute("feedback", feedback);

        // Forward to success.jsp
        req.getRequestDispatcher("components/success.jsp").forward(req, resp);
    }
}
