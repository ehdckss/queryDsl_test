package com.example.queryDsl.entity.response;

import com.example.queryDsl.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorAndBookResponse {

    private Long id;
    private String name;

    private List<Book> books;

}

