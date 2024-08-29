package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Comments;
import com.PizzaKoala.Pizza.domain.entity.Likes;
import com.PizzaKoala.Pizza.domain.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Long> {

    Page<Comments> findAllByPostId(Post post, Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE Comments i SET i.deletedAt = CURRENT_TIMESTAMP WHERE i.postId.id = :postId AND i.deletedAt IS NULL")
    void softDeleteByPostId(Long postId);

}
