package com.feedback.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "feedbackForm")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mobile", nullable = false)
    private String mobile;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "feedback", nullable = false)
    private String feedback;

    //    Default constructor
    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
