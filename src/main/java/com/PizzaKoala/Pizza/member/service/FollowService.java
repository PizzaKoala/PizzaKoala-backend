package com.PizzaKoala.Pizza.member.service;

import com.PizzaKoala.Pizza.global.controller.request.AlarmRequest;
import com.PizzaKoala.Pizza.global.exception.ErrorCode;
import com.PizzaKoala.Pizza.global.exception.PizzaAppException;
import com.PizzaKoala.Pizza.global.repository.AlarmRepository;
import com.PizzaKoala.Pizza.global.service.AlarmService;
import com.PizzaKoala.Pizza.member.dto.FollowListDTO;
import com.PizzaKoala.Pizza.global.entity.enums.AlarmType;
import com.PizzaKoala.Pizza.member.entity.Follow;
import com.PizzaKoala.Pizza.member.entity.Member;
import com.PizzaKoala.Pizza.member.repository.CustomFollowRepository;
import com.PizzaKoala.Pizza.member.repository.FollowRepository;
import com.PizzaKoala.Pizza.member.repository.MemberRepository;
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
    private final AlarmRepository alarmRepository;
    private final AlarmService alarmService;


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
            throw new PizzaAppException(ErrorCode.ALREADY_FOLLOWED, String.format("You have already followed %s",followingMember.getNickName()));
        }

        //TODO: 이거 두개 한꺼번에 호출하자!
        //https://chatgpt.com/share/6723196b-e108-800d-8e3b-59c86902d3fc
        AlarmRequest request = new AlarmRequest(
                AlarmType.NEW_FOLLOWER,
                following,
                member,
                null,
                null
        );
        alarmService.saveAndSend(request);


        return null;

    }


    public Void unfollow(String email, Long followingId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded", email)));
        if (member.getId().equals(followingId)) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, "You cannot unfollow yourself");
        }
        Follow follow = followRepository.findByFollowerIdAndFollowingId(member.getId(), followingId).orElseThrow(() -> new PizzaAppException(ErrorCode.FOLLOW_NOT_FOUND, "You are not following this user."));
        try {
            followRepository.delete(follow);
        } catch (Exception exception) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"Failed to delete.");
        }
        return null;
    }

    public Void deleteAFollower(String email, Long followerId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded", email)));

        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId,member.getId()).orElseThrow(() -> new PizzaAppException(ErrorCode.FOLLOW_NOT_FOUND, "This user is not following you."));
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
