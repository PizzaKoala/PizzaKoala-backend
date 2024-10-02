package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Likes;
import com.PizzaKoala.Pizza.global.entity.LikesType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByMemberIdAndLikesTypeIdAndLikesType(Long memberId, Long postId,LikesType likesType);
    @Modifying
    @Transactional
    @Query("update Likes l set l.deletedAt = null where l.id=:id")
    void updateDeletedAtToNull(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Likes i SET i.deletedAt = CURRENT_TIMESTAMP WHERE i.id = :id AND i.deletedAt IS NULL")
    void softDeleteById(Long id);


    void deleteByLikesTypeIdAndLikesType(Long postId, LikesType likesType);
}
