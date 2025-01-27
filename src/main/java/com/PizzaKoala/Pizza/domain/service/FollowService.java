package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.*;
import com.PizzaKoala.Pizza.domain.controller.request.AlarmRequest;
import com.PizzaKoala.Pizza.domain.entity.Follow;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.ProfileImage;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.domain.model.FollowListDTO;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.global.entity.AlarmType;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final AlarmService alarmService;
    private final CustomFollowRepository customFollowRepository;


    public Void follow(String email, Long following) {

        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not found", email)));

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
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not found", email)));
        if (member.getId().equals(followingId)) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, "You cannot unfollow yourself");
        }
        memberRepository.findById(followingId).orElseThrow(() -> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, "The specified member does not exist."));
        Follow follow = followRepository.findByFollowerIdAndFollowingId(member.getId(), followingId).orElseThrow(() -> new PizzaAppException(ErrorCode.FOLLOW_NOT_FOUND, "You are not following this user."));
        try {
            followRepository.delete(follow);
        } catch (Exception exception) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"Failed to delete.");
        }
        return null;
    }

    public Void deleteAFollower(String email, Long followerId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not found", email)));

        if (member.getId().equals(followerId)) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, "You cannot perform this action on your own account.");
        }
        memberRepository.findById(followerId).orElseThrow(() -> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, "The specified member does not exist."));

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
