package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL")
    Page<Post> findAllNonDeleted(Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.member= :member And p.deletedAt IS NULL")
    Page<Post> findAllByMemberAndNotDeleted(Member member, Pageable pageable);

//    @Query("SELECT p FROM Post p WHERE p.createdAt= :")
}
