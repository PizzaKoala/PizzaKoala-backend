package com.PizzaKoala.Pizza.member.repository;

import com.PizzaKoala.Pizza.member.dto.FollowListDTO;
import com.PizzaKoala.Pizza.member.entity.QFollow;
import com.PizzaKoala.Pizza.member.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomFollowRepositoryImpl implements CustomFollowRepository{
    private final JPAQueryFactory queryFactory;
    public CustomFollowRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     *
     * List of users following the user.
     *
     */

    public Page<FollowListDTO> myFollowerList(Long id, Pageable pageable) {
        QFollow qFollow = QFollow.follow;
        QMember qMember = QMember.member;
//        // Fetch profile image, nickname and id from member and joing follow-follower.
        List<Tuple> rawResults = queryFactory
                .select(qMember.id, qMember.nickName, qMember.profileImageUrl)
                .from(qMember)
                .join(qFollow).on(qFollow.followerId.eq(qMember.id))
                .where(qFollow.followingId.eq(id).and(qMember.deletedAt.isNull()))
                .orderBy(qFollow.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<FollowListDTO> finalResults = rawResults.stream().map(tuple -> {
            Long is = tuple.get(qMember.id);
            String nickname = tuple.get(qMember.nickName);
            String profileImageUrl = tuple.get(qMember.profileImageUrl);

            return new FollowListDTO(is, nickname, profileImageUrl);
        }).toList();
        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qMember.id.count())
                .from(qMember)
                .join(qFollow).on(qFollow.followerId.eq(qMember.id))
                .where(qFollow.followingId.eq(id).and(qMember.deletedAt.isNull()))
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount != null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }

    /**
     *
     * List of users that the user is following.
     *
     */
    public Page<FollowListDTO> myFollowingList(Long id, Pageable pageable) {
        QFollow qFollow = QFollow.follow;
        QMember qMember = QMember.member;
//        // Fetch profile image, nickname and id from member and joing follow-follower.

        List<Tuple> rawResults = queryFactory
                .select(qMember.id, qMember.nickName, qMember.profileImageUrl)
                .from(qMember)
                .join(qFollow).on(qFollow.followingId.eq(qMember.id))
                .where(qFollow.followerId.eq(id).and(qMember.deletedAt.isNull()))
                .orderBy(qFollow.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<FollowListDTO> finalResults = rawResults.stream().map(tuple -> {
            Long is = tuple.get(qMember.id);
            String nickname = tuple.get(qMember.nickName);
            String profileImageUrl = tuple.get(qMember.profileImageUrl);
          
            return new FollowListDTO(is,nickname,profileImageUrl);

        }).toList();
        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qMember.id.count())
                .from(qMember)
                .join(qFollow).on(qFollow.followingId.eq(qMember.id))
                .where(qFollow.followerId.eq(id).and(qMember.deletedAt.isNull()))
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }





}

