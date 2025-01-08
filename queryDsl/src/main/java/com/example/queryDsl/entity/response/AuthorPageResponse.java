package com.example.queryDsl.entity.response;

import java.util.List;

public class AuthorPageResponse {
    private long totalCount;
    private List<AuthorResponse> authors;

    // Constructor, Getters, Setters
    public AuthorPageResponse(long totalCount, List<AuthorResponse> authors) {
        this.totalCount = totalCount;
        this.authors = authors;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public List<AuthorResponse> getAuthors() {
        return authors;
    }
}
