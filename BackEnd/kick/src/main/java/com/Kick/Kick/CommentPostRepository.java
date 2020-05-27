package com.Kick.Kick;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CommentPostRepository extends PagingAndSortingRepository<CommentPost, Long> {
  Optional<CommentPost> findByUserAndPostAndComment(ApplicationUser user, Post post, String comment);
}
