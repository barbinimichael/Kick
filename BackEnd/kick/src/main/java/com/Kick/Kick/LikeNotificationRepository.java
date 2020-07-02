package com.Kick.Kick;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LikeNotificationRepository extends PagingAndSortingRepository<LikeNotification, Long> {
  Page<LikeNotification> findAllByUserLiked(String userLiked, Pageable pageable);
  Page<LikeNotification> findAllByPostOwnerUsername(String username, Pageable pageable);
}
