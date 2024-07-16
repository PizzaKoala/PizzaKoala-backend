package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.QImages;
import com.PizzaKoala.Pizza.domain.entity.QPost;
import com.PizzaKoala.Pizza.domain.model.PostListDTO;
import com.PizzaKoala.Pizza.domain.model.PostSummaryDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {
    private final JPAQueryFactory queryFactory;

    public CustomPostRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
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
    public Page<PostSummaryDTO> recentPosts(Pageable pageable) {
        QPost qPost = QPost.post;
        QImages qImages = QImages.images;

        // Fetch the post data with one image URL
        List<Tuple> rawResults = queryFactory
                .select(qPost.id, qPost.title, qImages.url.min(), qImages.id.countDistinct())
                .from(qPost)
                .leftJoin(qPost.images, qImages)
                .where(qPost.deletedAt.isNull())
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
                .where(qPost.deletedAt.isNull())
                .fetchOne();

        // Check for null totalCount
        long total = (totalCount!=null) ? totalCount : 0L;

        return new PageImpl<>(finalResults, pageable, total);
    }
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
}