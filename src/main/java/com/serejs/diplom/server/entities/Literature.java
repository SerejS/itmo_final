package com.serejs.diplom.server.entities;

import com.serejs.diplom.server.enums.LiteratureSource;

import javax.persistence.*;

@Entity
@Table(name = "literature")
public class Literature {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String path;
    private LiteratureSource source;

    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "type")
    private LiteratureType type;

    @OneToOne
    private LiteratureFormat format;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}