package com.example.demo.entity;
/*
 * Date: 12/28/20
 * Time: 9:10 AM
 * */

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 10)
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "body")
    private String body;

    @Column(name = "last_view_at", columnDefinition = "timestamp default null")
    private Date lastViewAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "theme_id")
    private Theme theme;


    public Question() {
    }

    public Question(@NotBlank @Size(min = 10) String title, @NotBlank String body, User user) {
        this.title = title;
        this.body = body;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '}';
    }
}
