package com.PizzaKoala.Pizza.domain.dto;

import com.PizzaKoala.Pizza.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Getter
public class CustomUserDetailsDTO implements UserDetails {
    private final Member member;


    public CustomUserDetailsDTO(Member member) {
        this.member = member;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(member.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    public Long getId() {
        return member.getId();
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
