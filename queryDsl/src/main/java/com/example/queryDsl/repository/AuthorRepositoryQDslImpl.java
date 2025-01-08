package com.example.queryDsl.repository;

import com.example.queryDsl.entity.response.AuthorAndBookResponse;
import com.example.queryDsl.entity.response.AuthorPageResponse;
import com.example.queryDsl.entity.response.AuthorResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.queryDsl.entity.QAuthor.author;
import static com.example.queryDsl.entity.QBook.book;

@Repository
public class AuthorRepositoryQDslImpl implements AuthorRepositoryQDsl{

    private final JPAQueryFactory queryFactory;

    // JPAQueryFactory 초기화
    public AuthorRepositoryQDslImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<AuthorResponse> selectAllAuthor(){
        return queryFactory
                .select(
                        Projections.fields(AuthorResponse.class, author.id, author.name)
                )
                .from(author)
                .fetch();
    }

    public CompletableFuture<AuthorPageResponse> selectAuthorsWithPaging(int page, int size) {
        // 첫 번째 쿼리: 총 레코드 수 (count)
        CompletableFuture<Long> totalCountFuture = CompletableFuture.supplyAsync(() -> {
            return queryFactory
                    .select(author.count())
                    .from(author)
                    .fetchOne();
        });

        // 두 번째 쿼리: 페이징된 데이터
        CompletableFuture<List<AuthorResponse>> authorsFuture = CompletableFuture.supplyAsync(() -> {
            return queryFactory
                    .select(Projections.fields(AuthorResponse.class, author.id, author.name))
                    .from(author)
                    .offset((page - 1) * size)
                    .limit(size)
                    .fetch();
        });

        // 병렬로 두 쿼리 처리 후 결합
        return CompletableFuture.allOf(totalCountFuture, authorsFuture)
                .thenApplyAsync(v -> {
                    try {
                        long totalCount = totalCountFuture.get();
                        List<AuthorResponse> authors = authorsFuture.get();
                        return new AuthorPageResponse(totalCount, authors);
                    } catch (Exception e) {
                        throw new RuntimeException("Error fetching data", e);
                    }
                });
    }


    public AuthorResponse selectAuthorByid(Long id){
        return queryFactory
                .select(
                        Projections.fields(AuthorResponse.class, author.id, author.name)
                )
                .from(author)
                .where(idEq(id))
                .fetchOne();
    }

    public AuthorAndBookResponse selectAuthorAndBookById(Long id){
        return queryFactory
                .select(
                        Projections.fields(AuthorAndBookResponse.class, author.id, author.name, author.books)
                )
                .from(author)
                .leftJoin(book).on(author.id.eq(book.id))
                .fetchOne();
    }


    private BooleanExpression idEq(Long id){
        return id != null ? author.id.eq(id) : null;
    }

}
