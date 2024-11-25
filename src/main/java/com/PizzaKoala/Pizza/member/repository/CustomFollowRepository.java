package com.PizzaKoala.Pizza.member.repository;

import com.PizzaKoala.Pizza.member.dto.FollowListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFollowRepository {
    public Page<FollowListDTO> myFollowerList(Long id, Pageable pageable);
    public Page<FollowListDTO> myFollowingList(Long id, Pageable pageable);
}
