package com.Kick.Kick;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FollowingRepository extends PagingAndSortingRepository<Following, Long> {
  Optional<Following> findByFollowerUsernameAndInfluencerUsername(String followerUsername, String influencerUsername);
}
