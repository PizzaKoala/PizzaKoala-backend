package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Member findByMyEmail(String email);
    Optional<Member> findByNickName(String nickName);


    boolean existsByNickName(String nickname);

    @Query("SELECT m FROM Member m WHERE m.socialLoginUsername = :socialLoginUsername")
    Member findBySocialLoginUsername(@Param("socialLoginUsername") String socialLoginUsername);

}
