package com.example.queryDsl.repository;

import com.example.queryDsl.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> , AuthorRepositoryQDsl {

    @Modifying
    @Transactional
    @Query(value = "DELETE from author", nativeQuery = true)
    void dleteAllAuthor();
}
