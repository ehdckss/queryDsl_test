package com.example.queryDsl.service;

import com.example.queryDsl.entity.Author;
import com.example.queryDsl.entity.response.AuthorPageResponse;
import com.example.queryDsl.entity.response.AuthorResponse;
import com.example.queryDsl.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    public void saveAuthor(List<Author> list){
        authorRepository.saveAll(list);
    }

    public List<AuthorResponse> selectAllAuthor(){
        return authorRepository.selectAllAuthor();
    }

    public AuthorResponse selectAuthorByid(Long id){
        return authorRepository.selectAuthorByid(id);
    }

    public CompletableFuture<AuthorPageResponse> selectAuthorsWithPaging(int page, int size){
        return authorRepository.selectAuthorsWithPaging(page, size);
    }

}
