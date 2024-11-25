package com.PizzaKoala.Pizza.domain.repository;

import com.PizzaKoala.Pizza.member.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.*;
import com.PizzaKoala.Pizza.domain.dto.PostSummaryDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {
    private final JPAQueryFactory queryFactory;

    public CustomPostRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     *
     * 맴버의 프로필 페이지-포스트들 가져오기
     *
     */
    public Page<PostSummaryDTO> memberPosts(Member member, Pageable pageable) {
        QPost qPost = QPost.post;
        QImages qImages = QImages.images;

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
                .from(qPost)
                .leftJoin(qPost.images, qImages)
                .where(qPost.member.id.eq(member.getId())
                        .and(qPost.deletedAt.isNull()))
                .groupBy(qPost.id, qPost.title)
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<PostSummaryDTO> finalResults = rawResults.stream().map(tuple -> {
            Long postId = tuple.get(qPost.id);
            String title = tuple.get(qPost.title);
            String imageUrl = tuple.get(qImages.url.min());
            Long imageCount = tuple.get(qImages.id.countDistinct());
            return new PostSummaryDTO(postId, title, imageUrl, imageCount);
        }).collect(Collectors.toList());



        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qPost.id.count())
                .from(qPost)
                .where(qPost.member.id.eq(member.getId())
                        .and(qPost.deletedAt.isNull()))
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }

//    /**
//     *
//     * 최근에 올라온 포스트들 가져오기 (public)
//     *
//     */
//    public Page<PostSummaryDTO> recentPosts(Pageable pageable) {
//        QPost qPost = QPost.post;
//        QImages qImages = QImages.images;
//
//        // Fetch the post data with one image URL
//        List<Tuple> rawResults = queryFactory
//                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
//                .from(qPost)
//                .leftJoin(qPost.images, qImages)
//                .where(qPost.deletedAt.isNull())
//                .groupBy(qPost.id, qPost.title)
//                .orderBy(qPost.createdAt.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        // Transform the results into DTOs
//        List<PostSummaryDTO> finalResults = rawResults.stream().map(tuple -> {
//            Long postId = tuple.get(qPost.id);
//            String title = tuple.get(qPost.title);
//            String imageUrl = tuple.get(qImages.url.min());
//            Long imageCount = tuple.get(qImages.id.countDistinct());
//            return new PostSummaryDTO(postId, title, imageUrl, imageCount);
//        }).toList();
//        // Fetch the total count of posts
//        Long totalCount = queryFactory
//                .select(qPost.id.count())
//                .from(qPost)
//                .where(qPost.deletedAt.isNull())
//                .fetchOne();
//
//        // Check for null totalCount
//        long total = (totalCount!=null) ? totalCount : 0L;
//
//        return new PageImpl<>(finalResults, pageable, total);
//    }
    /**
     *
     * 메인페이지- 팔로잉한 맴버들의 포스트들 가져오기
     *
     */
    public Page<PostSummaryDTO> followingPosts(Pageable pageable,Long id) {
        QPost qPost = QPost.post;
        QImages qImages = QImages.images;
        QFollow qFollow = QFollow.follow;

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
                .from(qPost)
                .leftJoin(qPost.images, qImages)
                .where(qPost.deletedAt.isNull().and(qPost.member.id.in(
                        JPAExpressions.select(qFollow.followingId).from(qFollow)
                                .where(qFollow.followerId.eq(id))
                )))
                .groupBy(qPost.id, qPost.title)
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<PostSummaryDTO> finalResults = rawResults.stream().map(tuple -> {
            Long postId = tuple.get(qPost.id);
            String title = tuple.get(qPost.title);
            String imageUrl = tuple.get(qImages.url.min());
            Long imageCount = tuple.get(qImages.id.countDistinct());
            return new PostSummaryDTO(postId, title, imageUrl, imageCount);
        }).toList();
        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qPost.id.count())
                .from(qPost)
                .where(qPost.deletedAt.isNull()
                        .and(qPost.member.id.in(
                                JPAExpressions.select(qFollow.followingId).from(qFollow)
                                        .where(qFollow.followerId.eq(id)))))
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }
//    /**
//     *
//     * 최근에 올라온 포스트들 가져오기 (public)
//     *
//     */
//    public Page<PostSummaryDTO> recentPosts(Pageable pageable) {
//        QPost qPost = QPost.post;
//        QImages qImages = QImages.images;
//
//        // Fetch the post data with one image URL
//        List<Tuple> rawResults = queryFactory
//                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
//                .from(qPost)
//                .leftJoin(qPost.images, qImages)
//                .where(qPost.deletedAt.isNull())
//                .groupBy(qPost.id, qPost.title)
//                .orderBy(qPost.createdAt.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        // Transform the results into DTOs
//        List<PostSummaryDTO> finalResults = rawResults.stream().map(tuple -> {
//            Long postId = tuple.get(qPost.id);
//            String title = tuple.get(qPost.title);
//            String imageUrl = tuple.get(qImages.url.min());
//            Long imageCount = tuple.get(qImages.id.countDistinct());
//            return new PostSummaryDTO(postId, title, imageUrl, imageCount);
//        }).toList();
//        // Fetch the total count of posts
//        Long totalCount = queryFactory
//                .select(qPost.id.count())
//                .from(qPost)
//                .where(qPost.deletedAt.isNull())
//                .fetchOne();
//
//        // Check for null totalCount
//        long total = (totalCount!=null) ? totalCount : 0L;
//
//        return new PageImpl<>(finalResults, pageable, total);
//    }
    /**
     *
     * 좋아요가 많이 눌린 포스트 가져오기
     *
     */
    public Page<PostSummaryDTO> likedPosts(Pageable pageable){
        QPost qPost = QPost.post;
        QImages qImages = QImages.images;

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
                .from(qPost)
                .leftJoin(qPost.images, qImages)
                .where(qPost.deletedAt.isNull())
                .groupBy(qPost.id, qPost.title)
                .orderBy(qPost.likes.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<PostSummaryDTO> finalResults = rawResults.stream().map(tuple -> {
            Long postId = tuple.get(qPost.id);
            String title = tuple.get(qPost.title);
            String imageUrl = tuple.get(qImages.url.min());
            Long imageCount = tuple.get(qImages.id.countDistinct());
            return new PostSummaryDTO(postId, title, imageUrl, imageCount);
        }).toList();
        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qPost.id.count())
                .from(qPost)
                .where(qPost.deletedAt.isNull())
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }
    /**
     *
     * 키워드가 들어간 최신순 포스트들 가져오기(제복,설명란)
     *
     */
    public Page<PostSummaryDTO> searchRecentPosts(String keyword, Pageable pageable) {
        QPost qPost = QPost.post;
        QImages qImages = QImages.images;
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.or(qPost.desc.containsIgnoreCase(keyword));
            builder.or(qPost.title.containsIgnoreCase(keyword));
        }

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
                .from(qPost)
                .leftJoin(qPost.images, qImages)
                .where(builder.and(qPost.deletedAt.isNull()))
                .groupBy(qPost.id, qPost.title)
                .orderBy(qPost.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<PostSummaryDTO> finalResults = rawResults.stream().map(tuple -> {
            Long postId = tuple.get(qPost.id);
            String title = tuple.get(qPost.title);
            String imageUrl = tuple.get(qImages.url.min());
            Long imageCount = tuple.get(qImages.id.countDistinct());
            return new PostSummaryDTO(postId, title, imageUrl, imageCount);
        }).collect(Collectors.toList());



        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qPost.id.count())
                .from(qPost)
                .where(builder
                        .and(qPost.deletedAt.isNull()))
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }

    /**
     *
     * 키워드가 들어간 포스트들 좋아요순으로 가져오기(제복,설명란)
     *
     */
    public Page<PostSummaryDTO> searchLikedPosts(String keyword, Pageable pageable) {
        QPost qPost = QPost.post;
        QImages qImages = QImages.images;
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.or(qPost.desc.containsIgnoreCase(keyword));
            builder.or(qPost.title.containsIgnoreCase(keyword));
        }

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
                .from(qPost)
                .leftJoin(qPost.images, qImages)
                .where(builder.and(qPost.deletedAt.isNull()))
                .groupBy(qPost.id, qPost.title)
                .orderBy(qPost.likes.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Transform the results into DTOs
        List<PostSummaryDTO> finalResults = rawResults.stream().map(tuple -> {
            Long postId = tuple.get(qPost.id);
            String title = tuple.get(qPost.title);
            String imageUrl = tuple.get(qImages.url.min());
            Long imageCount = tuple.get(qImages.id.countDistinct());
            return new PostSummaryDTO(postId, title, imageUrl, imageCount);
        }).collect(Collectors.toList());

        // Fetch the total count of posts
        Long totalCount = queryFactory
                .select(qPost.id.count())
                .from(qPost)
                .where(builder
                        .and(qPost.deletedAt.isNull()))
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }

    /**
     * 캘린더
     */
    public List<LocalDate> getPostsbyMonth(LocalDate startDate, LocalDate endDate, Long memberId) {
        QPost qPost = QPost.post;

        BooleanBuilder builder = new BooleanBuilder();

        builder.or(qPost.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));

        List<LocalDateTime> result= queryFactory
                .select(qPost.createdAt)
                .from(qPost)
                .where(builder.and(qPost.member.id.eq(memberId)))
                .distinct()
                .fetch();
        return result.stream()
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .toList();


    }
//    public List<LocalDateTime> getPostsbyYear(LocalDate startDate, LocalDate endDate, Long memberId) {
//        QPost qPost = QPost.post;
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        builder.or(qPost.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
//
//        return queryFactory
//                .select(qPost.createdAt)
//                .from(qPost)
//                .where(builder.and(qPost.member.id.eq(memberId)))
//                .distinct()
//                .fetch();
//
//
//    }
}