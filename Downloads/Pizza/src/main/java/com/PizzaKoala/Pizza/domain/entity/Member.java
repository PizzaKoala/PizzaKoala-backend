package com.PizzaKoala.Pizza.domain.entity;

import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.global.entity.CreatedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Getter
@EntityListeners(value = AuditingEntityListener.class)
public class Member extends CreatedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickName;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private LocalDateTime registeredAt;
    private LocalDateTime deletedAt;
    private String profileImageUrl;


    @PrePersist
    void registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

    public static Member of(String nickName, String email, String password, String url) {
        return Member.builder()
                .email(email)
                .nickName(nickName)
                .password(password)
                .role(MemberRole.USER)
                .profileImageUrl(url)
                .build();
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return  List.of(new SimpleGrantedAuthority(this.getRole().toString()));
//    }
//
//    @Override
//    public String getUsername() {
//        return this.email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return this.getDeletedAt() == null;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return this.getDeletedAt() == null;
//
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return this.getDeletedAt() == null;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return this.getDeletedAt() == null;
//    }
}
