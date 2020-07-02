package com.Kick.Kick;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FollowingNotificationRepository extends PagingAndSortingRepository<FollowingNotification, Long> {
  Page<FollowingNotification> findAllByInfluencerUsername(String influencer, Pageable pageable);
}
