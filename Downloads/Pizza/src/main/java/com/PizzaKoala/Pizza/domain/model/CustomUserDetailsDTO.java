package com.PizzaKoala.Pizza.domain.model;

import com.PizzaKoala.Pizza.domain.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetailsDTO implements UserDetails {
    private final Member member;

    public CustomUserDetailsDTO(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  List.of(new SimpleGrantedAuthority(member.getRole().toString()));


//        Collection<GrantedAuthority> collection = new ArrayList<>();
//        collection.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return member.getRole().toString();
//            }
//        });
    }


    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.getDeletedAt() == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getDeletedAt() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return member.getDeletedAt() == null;
    }

    @Override
    public boolean isEnabled() {
        return member.getDeletedAt() == null;
    }
}