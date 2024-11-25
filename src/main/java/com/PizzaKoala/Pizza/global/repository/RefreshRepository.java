package com.PizzaKoala.Pizza.global.repository;

import com.PizzaKoala.Pizza.member.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {
    Boolean existsByRefresh(String refresh);

//    @Transactional
//    void deleteByRefresh(String refresh);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken r SET r.refresh = null WHERE r.refresh = :refresh")
    void clearRefreshTokenByRefresh(String refresh);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken r SET r.refresh = :refresh, r.expiration = :expiration WHERE r.email = :email")
    void updateRefreshTokenByRefresh(@Param("refresh") String refresh,
                                     @Param("expiration") String expiration,
                                     @Param("email") String email);
    RefreshToken findByEmail(String email);
}
