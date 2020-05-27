package com.Kick.Kick;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface LikePostRepository extends PagingAndSortingRepository<LikePost, Long> {
  Optional<LikePost> findByUserAndPost(ApplicationUser user, Post post);
}
