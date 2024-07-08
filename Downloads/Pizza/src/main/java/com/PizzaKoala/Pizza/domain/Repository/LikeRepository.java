package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByMemberIdAndLikesTypeId(Long memberId, Long postId);

    Long findAllByLikesTypeId(Long likesTypeId);
}
