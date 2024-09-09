package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.AlarmRepository;
import com.PizzaKoala.Pizza.domain.Repository.FollowRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.ProfileImageRepository;
import com.PizzaKoala.Pizza.domain.entity.Follow;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.ProfileImage;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
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
        Follow follow = followRepository.findByFollowerIdAndFollowingId(member.getId(), followingId).orElseThrow(() -> new PizzaAppException(ErrorCode.FOLLOWING_NOT_FOUND, "You are not following this user."));
        try {
            followRepository.delete(follow);
        } catch (Exception exception) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"Failed to delete.");
        }
        return null;
    }
}
