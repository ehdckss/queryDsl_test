package com.example.queryDsl.repository;

import com.example.queryDsl.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> , AuthorRepositoryQDsl {

}
