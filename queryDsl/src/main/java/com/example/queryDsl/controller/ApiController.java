package com.example.queryDsl.controller;


import com.example.queryDsl.entity.Author;
import com.example.queryDsl.entity.response.AuthorResponse;
import com.example.queryDsl.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/authors")
@RestController
public class ApiController {

    @Autowired
    AuthorService authorService;


    // 전체 조회
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        List<AuthorResponse> authors = authorService.selectAllAuthor();
        return ResponseEntity.ok(authors);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable Long id) {
        AuthorResponse author = authorService.selectAuthorByid(id);
        return ResponseEntity.ok(author);
    }

    // 추가 또는 업데이트
    @PostMapping
    public ResponseEntity<Long> updateAuthorNameById(@RequestBody Author author) {
        Long savedAuthor = authorService.updateAuthorNameById(author.getId(),author.getName());
        return ResponseEntity.ok(savedAuthor);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthorsByCondition(id);
        return ResponseEntity.noContent().build();
    }

}
