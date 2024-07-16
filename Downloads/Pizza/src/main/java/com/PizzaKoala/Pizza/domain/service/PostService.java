package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.*;
import com.PizzaKoala.Pizza.domain.controller.Response.PostListResponse;
import com.PizzaKoala.Pizza.domain.entity.*;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.*;

import com.PizzaKoala.Pizza.global.entity.AlarmType;
import com.PizzaKoala.Pizza.global.entity.LikesType;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final ImageRepository imageRepository;
    private final CustomPostRepository customPostRepository;

    /**
     * Create a post 게시글 쓰기
     */
    @Transactional
    public void create(List<MultipartFile> files,String email, String title, String desc) throws IOException {

        //find user
        Member member = getMemberByEmailOrException(email);

        //post save
        Post post=postRepository.save(Post.of(title,desc,member));

        try {
            List<Images> createdImages = uploadImages(files, post, member);
        } catch (PizzaAppException e) {
            postRepository.delete(post);
            throw e;  // Re-throw the exception to maintain original behavior
        }

        }
    /**
     * edit my post 내 게시글 수정하기
     */
    @Transactional
    public PostDTO modify(String email,List<MultipartFile> files,String title, String desc, Long postId) throws IOException {

        //find user
        Member member = getMemberByEmailOrException(email);

        //post existence
        Post post= getPostOrException(postId);

        //post permission
        if (post.getMember() != member) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, String.format("$S has no permission with %s", member.getNickName(), post.getTitle()));
        }

        post.update(title,desc);
        // 기존 파일들 삭제 로직
        List<Images> existingImages = imageRepository.findByPostId(post);
        imageRepository.deleteAll(existingImages);
        for (Images image : existingImages) {
            s3ImageUploadService.deleteFiles(image.getUrl());

        }

        // 새로운 파일들 업로드 및 저장 로직
        uploadImages(files, post, member);


        return PostDTO.fromPostEntity(postRepository.saveAndFlush(post));
        //수정되서 포스트를 리턴할떄 modifiedAt 과 createdAt이 같았다 (디비는 create랑 modified 잘 분류되어있었다. postRepository.save -->postRepository.saveAndFlush 바꾸었다
    }
    /**
     * delete a post- 게시글 단건 삭제
     */
    @Transactional
    public void delete(String email, Long postId) {

        //find user
        Member member = getMemberByEmailOrException(email);


        //post existence
        Post post= getPostOrException(postId);

        //post permission
        if (post.getMember() != member) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, String.format("$S has no permission with %s", member.getNickName(), post.getTitle()));
        }
        imageRepository.softDeleteByPostId(postId);
        commentRepository.softDeleteByPostId(postId);

        post.delete();
       postRepository.saveAndFlush(post);
    }


    /**
     * 게시물 단건 조회- get a post
     */
    public Page<PostWithCommentsDTO> getAPost(Long postId, Pageable pageable) {
        Post post= getPostOrException(postId);

        // Fetch the image URLs
        List<String> imageUrls = post.getImages().stream()
                .map(Images::getUrl)
                .toList();
        Page<CommentDTO> commentDTOPage = getComments(postId, pageable);

        PostWithCommentsDTO postWithCommentsDTO= new PostWithCommentsDTO(
                post.getMember().getId(),
                post.getMember().getProfileImageUrl(),
                post.getMember().getNickName(),
                postId,
                post.getTitle(),
                post.getDesc(),
                post.getLikes(),
                imageUrls,
                post.getCreatedAt(),
                post.getModifiedAt(),
                commentDTOPage
        );
        return new PageImpl<>(List.of(postWithCommentsDTO), pageable, 1);
    }

    /**
     * 최신 피드
     */
    public Page<PostSummaryDTO> list(Pageable pageable) {
       return customPostRepository.recentPosts(pageable);

    }
    /**
     * 좋아요순 피드
     */
    public Page<PostSummaryDTO> Likedlist(Pageable pageable) {
        return customPostRepository.likedPosts(pageable);

    }

    /**
     * 내 게시물 메인 페이지 -
     */
    public Page<PostSummaryDTO> my(String email, Pageable pageable) {
            Member member = getMemberByEmailOrException(email);
       return customPostRepository.memberPosts(member, pageable);
    }
    /**
     * 특정 멤버 게시물 메인 페이지 -
     */
    public Page<PostSummaryDTO> memberPosts(Long memberId, Pageable pageable) {
        Member member = getMemberByIdOrException(memberId);
        return customPostRepository.memberPosts(member, pageable);
    }

    /**
     * 좋아요 기능
     */
    @Transactional
    public void likes(Long postId, String email) {

        //find user
        Member member = getMemberByEmailOrException(email);

        //post existence
        Post post= getPostOrException(postId);

        //check like existence -> throw
        if (likeRepository.findByMemberIdAndLikesTypeId(member.getId(), postId).isPresent())
            throw new PizzaAppException(ErrorCode.ALREADY_LIKED, String.format("%S already liked the post %d", member.getNickName(),postId));


        //like save
        likeRepository.save(Likes.of(member.getId(), LikesType.POST, postId));
        post.likes();
        postRepository.saveAndFlush(post);

        alarmRepository.save(Alarm.of(post.getMember().getId(), AlarmType.NEW_Like_ON_POST, new AlarmArgs(member.getId(),postId)));

    }
    /**
     * 좋아요 기능
     */
    public Long likeCount(Long postId) {
        Post post= getPostOrException(postId);

        return post.getLikes();

    }
    /**
     * 포스트에 댓글달기
     */
    @Transactional
    public void comment(Long postId, String email, String comment) {
        //post existence
         Post post= getPostOrException(postId);
        Member member = getMemberByEmailOrException(email);


        commentRepository.save(Comments.of(member, post, comment));
        alarmRepository.save(Alarm.of(post.getMember().getId(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(member.getId(), postId)));
    }
    /**
     * 포스트에 달린 댓글 보기 - member- id, nickname, profileUrl comment- 시간,커멘트
     */
    public Page<CommentDTO> getComments(Long postId, Pageable pageable) {
        Post post= getPostOrException(postId);
        return commentRepository.findAllByPostId(post, pageable).map(CommentDTO::fromCommentEntity);
    }
    /**
     * 포스트에 쓴 댓글 삭제 -
     */
    public Boolean commentDelete(Long postId, Long commentId, String email) {
        Optional<Comments> comments= commentRepository.findById(commentId);

        if (comments.isEmpty()) {
            throw new PizzaAppException(ErrorCode.COMMENT_NOT_FOUND);
        }
            Comments comment=comments.get();

        if (!comment.getPostId().getId().equals(postId)) {
            throw new PizzaAppException(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (!comment.getMember().getEmail().equals(email)) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION);
        }
        commentRepository.deleteById(commentId);
        return true;
    }








    private Post getPostOrException(Long postId) {
        //post existence
        Post post=postRepository.findById(postId).orElseThrow(()->
                new PizzaAppException(ErrorCode.POST_NOT_FOUND, String.format("%S not founded", postId)));
        if (post.getDeletedAt() != null) {
            throw new PizzaAppException(ErrorCode.POST_NOT_FOUND, String.format("%S not founded", postId));
        }
        return post;
    }
    public List<Images> uploadImages(List<MultipartFile> files, Post post, Member member) throws IOException {
        List<String> uploadedImageUrls = new ArrayList<>();
        List<Images> createdImages = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String imageUrl = s3ImageUploadService.upload(file);
                uploadedImageUrls.add(imageUrl);

                Images image = Images.of(post, member.getId(), imageUrl, file.getContentType(), file.getSize(), file.getOriginalFilename());
                Images savedImage = imageRepository.save(image);
                createdImages.add(savedImage);
            }
        } catch (Exception e) {
            for (String imageUrl : uploadedImageUrls) {
                s3ImageUploadService.deleteFiles(imageUrl);
            }
            imageRepository.deleteAll(createdImages);
            throw new PizzaAppException(ErrorCode.S3_UPLOAD_FAILED, e.getMessage());
        }

        return createdImages;
    }

    private Member getMemberByEmailOrException(String email) {
        //find member
        Member member=memberRepository.findByEmail(email).orElseThrow(() ->
                new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%S not found", email)));
        return member;
    }
    private Member getMemberByIdOrException(Long memberId) {
        //find member
        return memberRepository.findById(memberId).orElseThrow(() ->
                new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%S not found", memberId)));
    }



}
