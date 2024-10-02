package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long id, Long followingId);
}
