package com.feedback.dao;

import com.feedback.entity.Feedback;
import com.feedback.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class FeedbackDAO {
    // This class will handle database operations related to feedback
    // For example, saving feedback, retrieving feedback, etc.

    public boolean saveFeedBack(Feedback feedback) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(feedback);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Feedback getFeedbackById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Feedback.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Feedback> getAllFeedback() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Feedback", Feedback.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
