package com.example.queryDsl;

import com.example.queryDsl.entity.Author;
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
        for(long i=1;i<=10;i++){
            all.add(new Author("이름" + String.valueOf(i)));
        }
        authorService.saveAuthor(all);
    }

}
