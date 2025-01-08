package com.example.queryDsl.repository;

import com.example.queryDsl.entity.response.AuthorAndBookResponse;
import com.example.queryDsl.entity.response.AuthorPageResponse;
import com.example.queryDsl.entity.response.AuthorResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AuthorRepositoryQDsl {
    List<AuthorResponse> selectAllAuthor();

    public AuthorResponse selectAuthorByid(Long id);

    public AuthorAndBookResponse selectAuthorAndBookById(Long id);

    public CompletableFuture<AuthorPageResponse> selectAuthorsWithPaging(int page, int size);

    public long updateAuthorNameById(Long id, String newName);

    public long deleteAuthorsByCondition(Long id);

}
