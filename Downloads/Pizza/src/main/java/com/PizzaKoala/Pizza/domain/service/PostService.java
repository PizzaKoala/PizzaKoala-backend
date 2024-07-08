package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.*;
import com.PizzaKoala.Pizza.domain.entity.*;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.CommentDTO;
import com.PizzaKoala.Pizza.domain.model.PostDTO;
import com.PizzaKoala.Pizza.global.entity.AlarmType;
import com.PizzaKoala.Pizza.global.entity.LikesType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;
    @Transactional
    public void create(String email, String title, String desc) {
        //image's quantity check
//        List<MultipartFile> files
//        if (files.size() > 5||files.isEmpty()) {
//            throw new PizzaAppException(ErrorCode.IMAGE_UPLOAD_REQUIRED);
//        }
        //find user
        Member member = getMemberOrException(email);

        //post save
        Post saved=postRepository.save(Post.of(title,desc,member));
        //return
    }

    @Transactional
    public PostDTO modify(String email,String title, String desc, Long postId) {

        //find user
        Member member = getMemberOrException(email);

        //post existence
        Post post= getPostOrException(postId);

        //post permission
        if (post.getMember() != member) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, String.format("$S has no permission with %s", member.getNickName(), post.getTitle()));
        }

        post.update(title,desc);

        return PostDTO.fromPostEntity(postRepository.saveAndFlush(post));
        //수정되서 포스트를 리턴할떄 modifiedAt 과 createdAt이 같았다 (디비는 create랑 modified 잘 분류되어있었다. postRepository.save -->postRepository.saveAndFlush 바꾸었다
    }
    @Transactional
    public void delete(String email, Long postId) {

        //find user
        Member member = getMemberOrException(email);


        //post existence
        Post post= getPostOrException(postId);

        //post permission
        if (post.getMember() != member) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, String.format("$S has no permission with %s", member.getNickName(), post.getTitle()));
        }
        post.delete();
       postRepository.saveAndFlush(post);
    }

    /**
     * 최신 피드
     *
     *
     */
    public Page<PostDTO> list(Pageable pageable) {

         return postRepository.findAllNonDeleted(pageable).map(PostDTO::fromPostEntity);
    }

    public Page<PostDTO> my(String email,Pageable pageable) {
        //find user
        Member member = getMemberOrException(email);


        return postRepository.findAllByMemberAndNotDeleted(member,pageable).map(PostDTO::fromPostEntity);
    }
    @Transactional
    public void likes(Long postId, String email) {

        //find user
        Member member = getMemberOrException(email);

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

    public Long likeCount(Long postId) {
        Post post= getPostOrException(postId);

        return post.getLikes();

    }

    @Transactional
    public void comment(Long postId, String email, String comment) {
        //post existence
         Post post= getPostOrException(postId);
        Member member = getMemberOrException(email);


        commentRepository.save(Comments.of(member, post, comment));
        alarmRepository.save(Alarm.of(post.getMember().getId(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(member.getId(), postId)));
    }
    public Page<CommentDTO> getComments(Long postId, Pageable pageable) {
        Post post= getPostOrException(postId);
        return commentRepository.findAllByPostId(post, pageable).map(CommentDTO::fromCommentEntity);
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
    private Member getMemberOrException(String email) {
        //find member
        Member member=memberRepository.findByEmail(email).orElseThrow(() ->
                new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%S not found", email)));
        return member;
    }


}
