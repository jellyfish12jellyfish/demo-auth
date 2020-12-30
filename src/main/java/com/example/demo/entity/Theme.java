package com.example.demo.entity;
/*
 * Date: 12/30/20
 * Time: 11:30 AM
 * */

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5)
    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "question_id")
    private Question question;

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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", title='" + title + '}';
    }
}
