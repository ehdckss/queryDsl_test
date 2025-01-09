package com.example.queryDsl.service;

import com.example.queryDsl.entity.Author;
import com.example.queryDsl.entity.Book;
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
        List<Book> books = new ArrayList<>();
        for(long i=1;i<=15;i++){
            Author author = new Author("이름" + String.valueOf(i));
            all.add(author);
            books.add(new Book("책이름" + String.valueOf(i) , author));
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
        AuthorResponse findAuthor = authorService.selectAuthorByid(3L);
        assertEquals("홍길동", findAuthor.getName());
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



    @Test
    public void updateAuthorNameById() {
        //
        long result = authorService.updateAuthorNameById(1l, "조동찬");

        log.info("result : [{}]", result);

        AuthorResponse authorResponse = authorService.selectAuthorByid(1l);
        assertEquals(authorResponse.getName(),"조동찬");


    }

    @Test
    public void dleteAllAuthor(){
        authorRepository.dleteAllAuthor();

        List<AuthorResponse> authorResponses = authorService.selectAllAuthor();
        assertEquals(authorResponses.size(), 0);
    }


}