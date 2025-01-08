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

    //read

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



    //update
    public long updateAuthorNameById(Long id, String newName) {
        // 특정 id를 가진 Author의 이름을 업데이트하는 예시
        return queryFactory
                .update(author)  // 업데이트 대상 엔티티 지정
                .set(author.name, newName)  // 수정할 필드 설정
                .where(idEq(id))  // 조건: id가 일치하는 경우
                .execute();  // 쿼리 실행
    }

    //delete
    public long deleteAuthorsByCondition(Long id) {
        // 동적 조건을 기반으로 Author 엔티티를 삭제하는 예시
        return queryFactory
                .delete(author)  // 삭제 대상 엔티티 지정
                .where(idEq(id))
                .execute();  // 쿼리 실행
    }


    private BooleanExpression idEq(Long id){
        return id != null ? author.id.eq(id) : null;
    }

}
