package com.example.queryDsl.controller;


import com.example.queryDsl.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ApiController {

    @Autowired
    AuthorService authorService;

    @PostMapping("/authors")
    public void selectAllAuthor(){

    }

}
