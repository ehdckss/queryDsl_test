package com.example.queryDsl.entity;

import jakarta.persistence.*;


@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)  // Many to One 관계
    @JoinColumn(name = "author_id")     // 외래 키로 사용할 컬럼 이름 지정
    private Author author;

    // 기본 생성자
    public Book() {}

    // 생성자
    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    // Getter, Setter
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
