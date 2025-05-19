package Servlets;

import com.feedback.dao.FeedbackDAO;
import com.feedback.entity.Feedback;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/hibernateFeedback")
public class HibernateFeedback extends HttpServlet {
    private FeedbackDAO feedbackDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        feedbackDAO = new FeedbackDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email").trim();
        String mobile = req.getParameter("mobile").trim();
        String feedback = req.getParameter("feedback").trim();

//        Create a new Feedback entity
        Feedback feedbackEntity = new Feedback();

//        Save using Hibernate
        boolean success = feedbackDAO.saveFeedBack(feedbackEntity);


        if (success) {
            System.out.println("Feedback saved successfully");
            req.setAttribute("feedback", feedback);
            req.setAttribute("email", email);
            req.setAttribute("mobile", mobile);

            req.getRequestDispatcher("components/succes.jsp").forward(req, resp);

        } else {
            System.out.println("Failed to save feedback");
        }
    }
}
