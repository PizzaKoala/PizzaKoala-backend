package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Comments;
import com.PizzaKoala.Pizza.domain.entity.Likes;
import com.PizzaKoala.Pizza.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Long> {

    Page<Comments> findAllByPostId(Post post, Pageable pageable);

}
