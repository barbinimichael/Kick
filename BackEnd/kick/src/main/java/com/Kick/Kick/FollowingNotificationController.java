package com.Kick.Kick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class FollowingNotificationController extends Controller {

  private final FollowingNotificationRepository followingNotificationRepository;
  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;

  @Autowired
  public FollowingNotificationController(FollowingNotificationRepository followingNotificationRepository, ApplicationUserRepository applicationUserRepository, FollowingRepository followingRepository) {
    this.followingNotificationRepository = followingNotificationRepository;
    this.applicationUserRepository = applicationUserRepository;
    this.followingRepository = followingRepository;
  }

  @GetMapping("/api/followingNotifications/single/{id}")
  public ResponseEntity getFollowingNotification(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<FollowingNotification> maybeFollowingNotification = followingNotificationRepository.findById(id);

    if (maybeFollowingNotification.isPresent()) {
      FollowingNotification followingNotification = maybeFollowingNotification.get();
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

      if (followingNotification.getInfluencer().getUsername().equals(user.getUsername())) {
        return ResponseEntity.ok(followingNotification);

      } else {
        return handleBadCredentials(user.getUsername());
      }
    }
    return handleNotFound(id.toString());
  }

  @GetMapping("/api/followingNotifications/all")
  public ResponseEntity getFollowingNotifications(Authentication authentication,
                                             Pageable pageable) {
      return ResponseEntity.ok(followingNotificationRepository.findAllByInfluencerUsername(authentication.getName(), pageable));
  }

  @DeleteMapping("/api/followingNotifications/single/{id}")
  public ResponseEntity deleteFollowingNotification(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<FollowingNotification> maybeFollowingNotification = followingNotificationRepository.findById(id);

    if (maybeFollowingNotification.isPresent()) {
      FollowingNotification followingNotification = maybeFollowingNotification.get();
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

      if (followingNotification.getInfluencer().getUsername().equals(user.getUsername())) {
        followingNotificationRepository.delete(followingNotification);
        return handleSuccess("Deleted Following Notification successfully");

      } else {
        return handleBadCredentials(user.getUsername());
      }
    }
    return handleNotFound(id.toString());
  }
}
