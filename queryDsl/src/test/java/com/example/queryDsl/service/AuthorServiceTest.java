package com.example.queryDsl.service;

import com.example.queryDsl.entity.Author;
import com.example.queryDsl.entity.response.AuthorPageResponse;
import com.example.queryDsl.entity.response.AuthorResponse;
import com.example.queryDsl.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthorServiceTest {
    private static final Logger log = LoggerFactory.getLogger(AuthorServiceTest.class);
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorService authorService;


    @BeforeEach
    public void initData(){
        List<Author> all = new ArrayList<>();
        for(long i=1;i<=10;i++){
            all.add(new Author("이름" + String.valueOf(i)));
        }
        authorService.saveAuthor(all);
    }

    @Test
    public void findAllList() {
        List<Author> all = authorRepository.findAll();
        assertEquals(15, all.size());
    }

    @Test
    public void selectAuthorByid() {
        AuthorResponse findAuthor = authorService.selectAuthorByid(2L);
        assertEquals("이름2", findAuthor.getName());
    }


    @Test
    public void selectAuthorsWithPaging(){
        CompletableFuture<AuthorPageResponse> res =
                authorService.selectAuthorsWithPaging(1, 10);

        try {
            List<AuthorResponse> authors = res.get().getAuthors();

            assertEquals(authors.size(),10);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }


}