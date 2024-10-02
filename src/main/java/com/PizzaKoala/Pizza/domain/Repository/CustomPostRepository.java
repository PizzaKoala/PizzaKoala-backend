package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.model.PostListDTO;
import com.PizzaKoala.Pizza.domain.model.PostSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    public Page<PostSummaryDTO> memberPosts(Member member, Pageable pageable);
//    public Page<PostSummaryDTO> recentPosts(Pageable pageable);
    public Page<PostSummaryDTO> followingPosts(Pageable pageable, Long id);
    public Page<PostSummaryDTO> likedPosts(Pageable pageable);
    public Page<PostSummaryDTO> searchRecentPosts(String keyword, Pageable pageable);
    public Page<PostSummaryDTO> searchLikedPosts(String keyword, Pageable pageable);

}
