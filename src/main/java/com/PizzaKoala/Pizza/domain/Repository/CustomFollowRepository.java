package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.model.FollowListDTO;
import com.PizzaKoala.Pizza.domain.model.SearchMemberNicknameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFollowRepository {
    public Page<FollowListDTO> myFollowerList(Long id, Pageable pageable);
    public Page<FollowListDTO> myFollowingList(Long id, Pageable pageable);


}
