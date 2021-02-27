package com.example.demo.entity;
/*
 * Date: 12/31/20
 * Time: 7:59 AM
 * */

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "user_question")
public class UserQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private Date lastViewAt;

    public UserQuestion() {
    }

    public UserQuestion(User user, Question question) {
        this.user = user;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLastViewAt() {
        return lastViewAt;
    }

    public void setLastViewAt(Date lastViewAt) {
        this.lastViewAt = lastViewAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserQuestion that = (UserQuestion) o;

        if (question != null ? !question.equals(that.question) : that.question != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return lastViewAt != null ? lastViewAt.equals(that.lastViewAt) : that.lastViewAt == null;
    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (lastViewAt != null ? lastViewAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserQuestion{" +
                "question=" + question +
                ", user=" + user +
                ", lastViewAt=" + lastViewAt +
                '}';
    }
}
