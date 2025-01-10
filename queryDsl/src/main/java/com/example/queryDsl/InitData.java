package com.example.queryDsl;

import com.example.queryDsl.entity.Author;
import com.example.queryDsl.entity.Book;
import com.example.queryDsl.service.AuthorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitData {
    @Autowired
    AuthorService authorService;

    @PostConstruct
    public void init(){
        List<Author> all = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        for(long i=1;i<=15;i++){
            Author author = new Author("이름" + String.valueOf(i));
            all.add(author);
            books.add(new Book("책이름" + String.valueOf(i) , author));
        }
        authorService.saveAuthor(all);
    }

}
