package com.example.demo.entity;
/*
 * Date: 12/31/20
 * Time: 7:59 AM
 * */

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_view_at")
    private Date lastViewAt;

    public UserQuestion() {
    }

    public UserQuestion(User user, Date lastViewAt) {
        this.user = user;
        this.lastViewAt = lastViewAt;
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
}
