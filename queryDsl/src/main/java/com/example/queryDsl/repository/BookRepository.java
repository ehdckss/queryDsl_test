package com.example.queryDsl.repository;

import com.example.queryDsl.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
