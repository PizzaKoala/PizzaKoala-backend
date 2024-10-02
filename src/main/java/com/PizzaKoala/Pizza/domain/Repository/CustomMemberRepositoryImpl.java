package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.QFollow;
import com.PizzaKoala.Pizza.domain.entity.QMember;
import com.PizzaKoala.Pizza.domain.entity.QPost;
import com.PizzaKoala.Pizza.domain.model.SearchMemberNicknameDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository {
    private final JPAQueryFactory queryFactory;

    public CustomMemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * search all the members whose nickname contains the keyword.
     * ORDER THE RESULT BY THE MOST RECENT POSTS MADE BY THE MEMBERS.
     */

    public Page<SearchMemberNicknameDTO> searchMemberByRecentPosts(@Param("keyword") String keyword, Pageable pageable) {
        QMember qMember = QMember.member;
        QPost qPost = QPost.post;

        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.isEmpty()) {
            builder.or(qMember.nickName.containsIgnoreCase(keyword));
        }

        //Subquery to get the most recent post date for each member
//        QPost usbQPost = new QPost("subQPost");
//        JPQLQuery<LocalDateTime> subQuery = queryFactory
//                .select(usbQPost.createdAt.max().as("recent"))
//                .from(usbQPost)
//                .where(usbQPost.member.eq(qMember));

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qMember.id, qMember.nickName,qMember.profileImageUrl)
                .leftJoin(qPost).on(qPost.member.eq(qMember))
                .from(qMember)
                .where(builder.and(qMember.deletedAt.isNull()))
                .groupBy(qMember.id,qMember.nickName,qMember.profileImageUrl)
                .orderBy(qPost.createdAt.max().desc())
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
                .where(builder.and(qMember.deletedAt.isNull()))
                .fetchOne();
        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;
        return new PageImpl<>(finalResults, pageable, total);

    }




    public Page<SearchMemberNicknameDTO> searchMemberByMostFollowers(String keyword, Pageable pageable) {
        QMember qMember = QMember.member;
        QFollow qFollow = QFollow.follow;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.or(qMember.nickName.containsIgnoreCase(keyword));
        }

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qMember.id, qMember.nickName,qMember.profileImageUrl)
                .from(qMember)
                .leftJoin(qFollow).on(qFollow.followingId.eq(qMember.id))
                .where(builder.and(qMember.deletedAt.isNull()))
                .groupBy(qMember.id,qMember.nickName,qMember.profileImageUrl)
                .orderBy(qFollow.followerId.count().desc())
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
                .leftJoin(qFollow).on(qFollow.followingId.eq(qMember.id))
                .where(builder
                        .and(qMember.deletedAt.isNull()))
                .orderBy(qFollow.followerId.count().desc())
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }
}