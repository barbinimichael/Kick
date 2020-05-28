package com.Kick.Kick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class FollowingController extends Controller {

  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  @Autowired
  public FollowingController(ApplicationUserRepository applicationUserRepository,
                             FollowingRepository followingRepository) {
    this.applicationUserRepository = applicationUserRepository;
    this.followingRepository = followingRepository;
  }

  @GetMapping("/api/followings/{id}")
  public ResponseEntity getFollowing(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<Following> maybeFollowing = followingRepository.findById(id);

    if (maybeFollowing.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Following following = maybeFollowing.get();

      if (user.equals(following.getFollower()) || user.equals(following.getInfluencer())) {
        return ResponseEntity.ok(maybeFollowing.get());
      } else {
        return handleBadCredentials(authentication.getName());
      }
    } else {
      return handleNotFound(id.toString());
    }
  }

  @PostMapping("/api/followings")
  public ResponseEntity follow(Authentication authentication, @Param("username") String username) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    Optional<ApplicationUser> maybeInfluencer = applicationUserRepository.findByUsername(username);

    if (maybeInfluencer.isPresent()) {
      logger.info("In following controller- post");
      ApplicationUser influencer = maybeInfluencer.get();
      Following newFollowing = new Following(user, influencer);

      if (!influencer.isPrivateProfile()) {
        newFollowing.setAccepted(true);
      }
      this.followingRepository.save(newFollowing);
      return handleSuccess("Saved following");
    } else {
      return handleNotFound(username);
    }
  }

  @PostMapping("/api/followings/{id}/{condition}")
  public ResponseEntity acceptFollower(Authentication authentication, @PathVariable @NonNull Long id, @PathVariable @NonNull boolean condition) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    Optional<Following> maybeFollowing = followingRepository.findById(id);

    if (maybeFollowing.isPresent()) {
      Following following = maybeFollowing.get();

      // check that it is the user who accepting is the user that was being requested
      if (user.getWhereIsInfluencer().contains(following)) {
        if (condition) {
          following.setAccepted(true);
          followingRepository.save(following);
          return handleSuccess("Accepted following");

        } else {
          followingRepository.delete(following);
          return handleSuccess("Dismissed following");
        }
      } else {
        return handleBadCredentials(user.getUsername());
      }
    } else {
      return handleNotFound(id.toString());
    }
  }

  @DeleteMapping("/api/followings/{id}")
  public ResponseEntity deleteFollowing(Authentication authentication, @PathVariable @NonNull Long id) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    Optional<Following> maybeFollowing = followingRepository.findById(id);

    if (maybeFollowing.isPresent()) {
      Following following = maybeFollowing.get();

      if (following.getInfluencer().equals(user) || following.getFollower().equals(user)) {
        followingRepository.delete(following);
        return handleSuccess("Deleted follow successfully");
      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }
}
