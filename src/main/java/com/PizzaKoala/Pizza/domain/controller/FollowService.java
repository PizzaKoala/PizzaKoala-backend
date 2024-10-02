package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.Repository.CustomFollowRepository;
import com.PizzaKoala.Pizza.domain.Repository.FollowRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.entity.Follow;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.FollowListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final CustomFollowRepository customFollowRepository;
    public Void follow(String email, Long following) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded", email)));
        if (member.getId().equals(following)) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, "You cannot follow your own account.");
        }
        Member followingMember = memberRepository.findById(following).orElseThrow(()->new PizzaAppException(ErrorCode.FOLLOWING_ID_NOT_FOUND));
        try {
            Follow follow = Follow.of(member.getId(), following);
            followRepository.save(follow);
        } catch (DataIntegrityViolationException exception) {
            throw new PizzaAppException(ErrorCode.ALREADY_FOLLOWED, String.format("You have are already followed %s",followingMember.getNickName()));
        }
        return null;
    }
    public Void unfollow(String email, Long followingId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded", email)));
        if (member.getId().equals(followingId)) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, "You cannot unfollow yourself");
        }
        Follow follow = followRepository.findByFollowerIdAndFollowingId(member.getId(), followingId).orElseThrow(() -> new PizzaAppException(ErrorCode.FOLLOWING_ID_NOT_FOUND, "You are not following this user."));
        try {
            followRepository.delete(follow);
        } catch (Exception exception) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"Failed to delete.");
        }
        return null;
    }

    public Void deleteAFollower(String email, Long followerId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded", email)));
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId,member.getId()).orElseThrow(() -> new PizzaAppException(ErrorCode.FOLLOWER_NOT_FOUND, "This user is not following you."));
        try {
            followRepository.delete(follow);
        } catch (Exception exception) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"Failed to delete.");
        }
        return null;
    }
    public Page<FollowListDTO> myFollowList(String email, Pageable pageable, int or) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND));
        switch (or) {
            case 1:
                return customFollowRepository.myFollowerList(member.getId(), pageable);
            case 2:
                return customFollowRepository.myFollowingList(member.getId(),pageable);
            default: throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"follower list-1, following list-2");
        }
    }
}
