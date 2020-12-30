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


    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", title='" + title + '}';
    }
}
