package com.example.queryDsl.repository;

import com.example.queryDsl.entity.response.AuthorAndBookResponse;
import com.example.queryDsl.entity.response.AuthorPageResponse;
import com.example.queryDsl.entity.response.AuthorResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
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


    public AuthorResponse selectAuthorByid2(Long id){
        StringTemplate nameTemplate = Expressions.stringTemplate(
                "CASE WHEN {0} = '이름3' THEN '홍길동' WHEN {0} = '이름4' THEN '김땡땡' ELSE {0} END",
                author.name
        );

        return queryFactory
                .select(
                        Projections.fields(AuthorResponse.class, author.id, nameTemplate.as("name"))
                )
                .from(author)
                .where(idEq(id))
                .fetchOne();

    }
    public AuthorResponse selectAuthorByid(Long id){
        StringExpression caseByName = new CaseBuilder()
                .when(author.name.eq("이름3")).then("홍길동")
                .when(author.name.eq("이름4")).then("유재석")
                .otherwise("이름들");

        return queryFactory
                .select(
                        Projections.fields(AuthorResponse.class, author.id, caseByName.as("name"))
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
    public long updateAuthorNameById(Long id, String newName) {
        return queryFactory
                .update(author)
                .set(author.name, newName)
                .where(idEq(id))
                .execute();
    }
    public long deleteAuthorsByCondition(Long id) {
        return queryFactory
                .delete(author)
                .where(idEq(id))
                .execute();
    }
    private BooleanExpression idEq(Long id){
        return id != null ? author.id.eq(id) : null;
    }

}
