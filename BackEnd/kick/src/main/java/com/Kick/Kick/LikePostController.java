package com.Kick.Kick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@RestController
public class LikePostController extends Controller {

  private final ApplicationUserRepository applicationUserRepository;
  private final LikePostRepository likePostRepository;
  private final PostRepository postRepository;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  @Autowired
  public LikePostController(ApplicationUserRepository applicationUserRepository,
                            LikePostRepository likePostRepository, PostRepository postRepository) {
    this.applicationUserRepository = applicationUserRepository;
    this.likePostRepository = likePostRepository;
    this.postRepository = postRepository;
  }

  @GetMapping("/api/posts/{id}/liked")
  public ResponseEntity getIfLikedPost(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();

      if (post.getLikes().stream().map(LikePost::getUser).anyMatch(n -> n.equals(user))) {
        return ResponseEntity.ok(true);
      } else {
        return ResponseEntity.ok(false);
      }
    } else {
      return handleNotFound(id.toString());
    }
  }

  @GetMapping("/api/likePosts/{id}")
  public ResponseEntity getLikePost(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<LikePost> maybeLikePost = likePostRepository.findById(id);

    if (maybeLikePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      LikePost likePost = maybeLikePost.get();

      if (!likePost.getUser().isPrivateProfile() || likePost.getUser().equals(user) || checkFollowing(user, likePost.getUser())) {
        return ResponseEntity.ok(maybeLikePost.get());
      } else {
        return handleBadCredentials(authentication.getName());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }

  @PostMapping("/api/posts/{id}/likePosts")
  public ResponseEntity like(Authentication authentication, @PathVariable @NonNull Long id) {

    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();

      if (!post.getUser().isPrivateProfile() ||
          post.getUser().equals(user) ||
          checkFollowing(user, post.getUser())) {

        // like only once
        if (post.getLikes().stream().map(LikePost::getUser).noneMatch(n -> n.equals(user))) {
          LikePost likePost = new LikePost(user, post);
          likePostRepository.save(likePost);
          return handleSuccess(String.valueOf((post.getLikes().size() + 1)));

        } else {
          return handleBadRequest("Like: " + id.toString());
        }

      } else {
        return handleBadCredentials(authentication.getName());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }

  @DeleteMapping("/api/likePosts/{id}")
  public ResponseEntity deleteLike(
      Authentication authentication, @PathVariable @NonNull Long id) {

    Optional<LikePost> maybeLikePost = likePostRepository.findById(id);

    if (maybeLikePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      LikePost likePost = maybeLikePost.get();
      Optional<ApplicationUser> likeUser =
          applicationUserRepository.findById(likePost.getUser().getId());

      if (likeUser.isPresent()) {
        if (likeUser.get().equals(user)) {
          likePostRepository.delete(likePost);
          return handleSuccess("Deleted like successfully");

        } else {
          return handleBadCredentials(authentication.getName());
        }

      } else {
        return handleNotFound(id.toString());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }
}
