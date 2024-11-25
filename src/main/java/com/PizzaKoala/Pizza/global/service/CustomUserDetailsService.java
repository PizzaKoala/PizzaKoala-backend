package com.PizzaKoala.Pizza.global.service;

import com.PizzaKoala.Pizza.member.repository.MemberRepository;
import com.PizzaKoala.Pizza.member.entity.Member;
import com.PizzaKoala.Pizza.domain.dto.CustomUserDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByMyEmail(email);
        if (member!=null) {
            return new CustomUserDetailsDTO(member);
        }else {
            log.error("Error: Member with email {} does not exist", email);
            throw new UsernameNotFoundException("Member with email " + email + "does not exist");
        }
    }
}
//UserEntity userData = userRepository.findByUsername(username);
//
//        if (userData != null) {
//
//        return new CustomUserDetails(userData);
//        }
//
//
//                return null;