package com.example.queryDsl.service;

import com.example.queryDsl.entity.Author;
import com.example.queryDsl.entity.response.AuthorPageResponse;
import com.example.queryDsl.entity.response.AuthorResponse;
import com.example.queryDsl.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Transactional
@Service
public class AuthorService {


    @Autowired
    AuthorRepository authorRepository;

    public void saveAuthor(List<Author> list){
        authorRepository.saveAll(list);
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> selectAllAuthor(){
        return authorRepository.selectAllAuthor();
    }

    @Transactional(readOnly = true)
    public AuthorResponse selectAuthorByid(Long id){
        return authorRepository.selectAuthorByid(id);
    }

    @Transactional(readOnly = true)
    public CompletableFuture<AuthorPageResponse> selectAuthorsWithPaging(int page, int size){
        return authorRepository.selectAuthorsWithPaging(page, size);
    }


    public long updateAuthorNameById(Long id, String newName){
        return authorRepository.updateAuthorNameById(id, newName);
    }


    public long deleteAuthorsByCondition(Long id){
        return authorRepository.deleteAuthorsByCondition(id);
    }

}
