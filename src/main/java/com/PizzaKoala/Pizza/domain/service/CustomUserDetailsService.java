package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.model.CustomUserDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if (member != null) {
            return new CustomUserDetailsDTO(member);
        } else {
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