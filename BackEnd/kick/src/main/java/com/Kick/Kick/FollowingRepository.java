package com.Kick.Kick;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FollowingRepository extends PagingAndSortingRepository<Following, Long> {
  Optional<Following> findByFollowerUsernameAndInfluencerUsername(String followerUsername, String influencerUsername);
  Page<Following> findAllByInfluencerUsernameAndAccepted(String influencerUsername, boolean accepted, Pageable pageable);
  Page<Following> findAllByFollowerUsernameAndAccepted(String followerUsername, boolean accepted, Pageable pageable);
}
