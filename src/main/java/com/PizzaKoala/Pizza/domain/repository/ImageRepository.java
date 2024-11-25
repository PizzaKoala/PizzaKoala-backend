package com.PizzaKoala.Pizza.domain.repository;

import com.PizzaKoala.Pizza.domain.entity.Images;
import com.PizzaKoala.Pizza.domain.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Images,Long> {
    List<Images> findByPostId(Post post);
    @Transactional
    @Modifying
    @Query("UPDATE Images i SET i.deletedAt = CURRENT_TIMESTAMP WHERE i.postId.id = :postId AND i.deletedAt IS NULL")
    void softDeleteByPostId(Long postId);


//    @Query("SELECT i FROM Images i JOIN FETCH i.postId WHERE i.memberId = :memberId AND i.deletedAt IS NULL")
//    List<Images> findByMemberIdAndNotDeleted(@Param("memberId") Long memberId);

//    Page<Alarm> findAllByMemberId(Long memberId, Pageable pageable);
}
