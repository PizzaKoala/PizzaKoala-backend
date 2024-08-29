package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.QImages;
import com.PizzaKoala.Pizza.domain.entity.QMember;
import com.PizzaKoala.Pizza.domain.entity.QPost;
import com.PizzaKoala.Pizza.domain.model.PostSummaryDTO;
import com.PizzaKoala.Pizza.domain.model.SearchMemberNicknameDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository {
    private final JPAQueryFactory queryFactory;

    public CustomMemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     *
     * search all the members, whose member nickname contains the keyword
     *
     */
//    @Query("SELECT m.id,m.nickName,m.profileImageUrl FROM Member m WHERE m.nickName like %:keyword% AND m.deletedAt=null")
//    Member searchKeywordByNickname(String keyword);
    public Page<SearchMemberNicknameDTO> searchKeywordByNickname(@Param("keyword") String keyword, Pageable pageable) {
        QMember qMember = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.or(qMember.nickName.containsIgnoreCase(keyword));
        }

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qMember.id, qMember.nickName,qMember.profileImageUrl)
                .from(qMember)
                .where(builder.and(qMember.deletedAt.isNull()))
                .orderBy(qMember.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<SearchMemberNicknameDTO> finalResults = rawResults.stream().map(tuple -> {
            Long id = tuple.get(qMember.id);
            String nickname = tuple.get(qMember.nickName);
            String profileImageUrl = tuple.get(qMember.profileImageUrl);
            return new SearchMemberNicknameDTO(id,nickname,profileImageUrl);
        }).collect(Collectors.toList());



        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qMember.id.count())
                .from(qMember)
                .where(builder
                        .and(qMember.deletedAt.isNull()))
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }


}