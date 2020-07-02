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
public class LikeNotificationController extends Controller {

  private final LikeNotificationRepository likeNotificationRepository;
  private final ApplicationUserRepository applicationUserRepository;

  @Autowired
  public LikeNotificationController(LikeNotificationRepository likeNotificationRepository, ApplicationUserRepository applicationUserRepository) {
    this.likeNotificationRepository = likeNotificationRepository;
    this.applicationUserRepository = applicationUserRepository;
  }

  @GetMapping("/api/likeNotifications/single/{id}")
  public ResponseEntity getLikeNotification(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<LikeNotification> maybeLikeNotification = likeNotificationRepository.findById(id);

    if (maybeLikeNotification.isPresent()) {
      LikeNotification likeNotification = maybeLikeNotification.get();
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

      if (likeNotification.getPostOwner().getUsername().equals(user.getUsername())) {
        return ResponseEntity.ok(likeNotification);

      } else {
        return handleBadCredentials(user.getUsername());
      }
    }
    return handleNotFound(id.toString());
  }

  @GetMapping("/api/likeNotifications/all")
  public ResponseEntity getLikeNotifications(Authentication authentication,
                                             Pageable pageable) {
      return ResponseEntity.ok(likeNotificationRepository.findAllByPostOwnerUsername(authentication.getName(), pageable));
  }

  @DeleteMapping("/api/likeNotifications/single/{id}")
  public ResponseEntity deleteLikeNotification(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<LikeNotification> maybeLikeNotification = likeNotificationRepository.findById(id);

    if (maybeLikeNotification.isPresent()) {
      LikeNotification likeNotification = maybeLikeNotification.get();
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

      if (likeNotification.getPostOwner().getUsername().equals(user.getUsername())) {
        likeNotificationRepository.delete(likeNotification);
        return handleSuccess("Deleted likeNotification successfully");

      } else {
        return handleBadCredentials(user.getUsername());
      }
    }
    return handleNotFound(id.toString());
  }
}
