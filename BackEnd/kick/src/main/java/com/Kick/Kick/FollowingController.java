package com.Kick.Kick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class FollowingController extends Controller {

  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;
  private final FollowingNotificationRepository followingNotificationRepository;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  @Autowired
  public FollowingController(ApplicationUserRepository applicationUserRepository,
                             FollowingRepository followingRepository, FollowingNotificationRepository followingNotificationRepository) {
    this.applicationUserRepository = applicationUserRepository;
    this.followingRepository = followingRepository;
    this.followingNotificationRepository = followingNotificationRepository;
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

  @GetMapping("/api/followings/check/{username}")
  public ResponseEntity isFollowing(Authentication authentication, @PathVariable @NonNull String username) {
    if (authentication.getName().equals(username)) {
      return ResponseEntity.ok("following");
    }

    Optional<ApplicationUser> maybeInfluencer = applicationUserRepository.findByUsername(username);

    if (maybeInfluencer.isPresent()) {
      ApplicationUser influencer = maybeInfluencer.get();
      ApplicationUser follower = applicationUserRepository.findByUsername(authentication.getName()).get();

      Optional<Following> maybeFollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(follower.getUsername(), influencer.getUsername());

      if (maybeFollowing.isPresent()) {
        Following following = maybeFollowing.get();

        if (following.isAccepted()) {
          return ResponseEntity.ok("following");
        } else{
          return ResponseEntity.ok("requested following");
        }

      }
    }
    return ResponseEntity.ok("not following");
  }

  @GetMapping("/api/followings/followers/{username}")
  public ResponseEntity getFollowers(Authentication authentication, @PathVariable @NonNull String username, Pageable pageable) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    if (authentication.getName().equals(username)) {
      logger.info("Following controller same user");
      return ResponseEntity.ok(this.getFollowers(followingRepository.findAllByInfluencerUsernameAndAccepted(user.getUsername(), true, pageable)));
    }

    Optional<ApplicationUser> maybeInfluencer = applicationUserRepository.findByUsername(username);

    if (maybeInfluencer.isPresent()) {
      logger.info("Following controller influencer present");
      ApplicationUser influencer = maybeInfluencer.get();

      if (!influencer.isPrivateProfile()) {
        logger.info("Following controller influencer was not private");
        return ResponseEntity.ok(this.getFollowers(followingRepository.findAllByInfluencerUsernameAndAccepted(influencer.getUsername(), true, pageable)));
      }

      Optional<Following> maybeFollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(user.getUsername(), influencer.getUsername());

      if (maybeFollowing.isPresent()) {
        logger.info("Following controller following present");
        Following following = maybeFollowing.get();

        if (following.isAccepted()) {
          logger.info("Following controller following accepted");
          return ResponseEntity.ok(this.getFollowers(followingRepository.findAllByInfluencerUsernameAndAccepted(influencer.getUsername(), true, pageable)));
        }

      }
    }
    return ResponseEntity.ok(new PageImpl<ApplicationUser>(new ArrayList<>(), pageable, 0));
  }

  @GetMapping("/api/followings/influencers/{username}")
  public ResponseEntity getInfluencers(Authentication authentication, @PathVariable @NonNull String username, Pageable pageable) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    if (authentication.getName().equals(username)) {
      return ResponseEntity.ok(this.getInfluencers(followingRepository.findAllByFollowerUsernameAndAccepted(user.getUsername(), true, pageable)));
    }

    Optional<ApplicationUser> maybeInfluencer = applicationUserRepository.findByUsername(username);

    if (maybeInfluencer.isPresent()) {
      ApplicationUser influencer = maybeInfluencer.get();

      if (!influencer.isPrivateProfile()) {
        return ResponseEntity.ok(this.getInfluencers(followingRepository.findAllByFollowerUsernameAndAccepted(influencer.getUsername(), true, pageable)));
      }

      Optional<Following> maybeFollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(user.getUsername(), influencer.getUsername());

      if (maybeFollowing.isPresent()) {
        Following following = maybeFollowing.get();

        if (following.isAccepted()) {
          return ResponseEntity.ok(this.getInfluencers(followingRepository.findAllByFollowerUsernameAndAccepted(influencer.getUsername(), true, pageable)));
        }

      }
    }
    return ResponseEntity.ok(new PageImpl<ApplicationUser>(new ArrayList<>(), pageable, 0));
  }

  @PostMapping("/api/followings/{username}")
  public ResponseEntity follow(Authentication authentication, @PathVariable @NonNull String username) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    Optional<ApplicationUser> maybeInfluencer = applicationUserRepository.findByUsername(username);

    if (maybeInfluencer.isPresent()) {
      ApplicationUser influencer = maybeInfluencer.get();
      Following newFollowing = new Following(user, influencer);

      if (followingRepository.findByFollowerUsernameAndInfluencerUsername(user.getUsername(), influencer.getUsername()).isEmpty()) {
        Following savedFollowing;

        if (!influencer.isPrivateProfile()) {
          newFollowing.setAccepted(true);
          savedFollowing = followingRepository.save(newFollowing);

        } else {
          savedFollowing = followingRepository.save(newFollowing);
          followingNotificationRepository.save(new FollowingNotification(influencer, savedFollowing.getId(), user.getUsername()));
        }

        return ResponseEntity.ok(savedFollowing);

      } else {
        return handleBadRequest("following already created for " + username);
      }

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
      // logger.info(authentication.getName() + " is accepting: " + String.valueOf(condition) + " user: " + following.getFollower());

      // check that it is the user who accepting is the user that was being requested
      if (user.getWhereIsInfluencer().contains(following)) {
        if (condition) {
          following.setAccepted(true);
          return ResponseEntity.ok(followingRepository.save(following));

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

  @DeleteMapping("/api/followings/follower/deleting/influencer/{follower}/{influencer}")
  public ResponseEntity userDeleteFollowing(Authentication authentication, @PathVariable @NonNull String follower, @PathVariable @NonNull String influencer) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    Optional<Following> maybeFollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(follower, influencer);

    if (maybeFollowing.isPresent()) {
      Following following = maybeFollowing.get();

      if (following.getInfluencer().getUsername().equals(influencer) && following.getFollower().getUsername().equals(follower)) {
        followingRepository.deleteById(following.getId());
        return handleSuccess("Deleted follow successfully");

      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(follower + " " + user);
    }
  }

  @DeleteMapping("/api/followings/influencer/deleting/follower/{influencer}/{follower}")
  public ResponseEntity influencerDeleteFollowing(Authentication authentication, @PathVariable @NonNull String follower, @PathVariable @NonNull String influencer) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    Optional<Following> maybeFollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(follower, influencer);

    if (maybeFollowing.isPresent()) {
      Following following = maybeFollowing.get();

      if (following.getInfluencer().getUsername().equals(influencer) && following.getFollower().getUsername().equals(influencer)) {
        followingRepository.deleteById(following.getId());
        return handleSuccess("Deleted follow successfully");
      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(follower + " " + user);
    }
  }

  private Page<ApplicationUser> getFollowers(Page<Following> followings) {
    return followings.map(Following::getFollower);
  }

  private Page<ApplicationUser> getInfluencers(Page<Following> influencers) {
    return influencers.map(Following::getInfluencer);
  }
}
