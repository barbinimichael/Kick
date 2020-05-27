package com.Kick.Kick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PostController extends Controller {

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  private PostRepository postRepository;
  private ApplicationUserRepository applicationUserRepository;

  @Autowired
  public PostController(ApplicationUserRepository applicationUserRepository,
                        PostRepository postRepository) {
    this.applicationUserRepository = applicationUserRepository;
    this.postRepository = postRepository;
  }

  // page, size, sort
  @GetMapping("/api/posts/feed")
  public ResponseEntity getFeed(Authentication authentication, Pageable pageable) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    ArrayList<ApplicationUser> users = new ArrayList<>();
    for (Following following : user.getInfluencers()) {
      if (following.isAccepted()) {
        users.add(following.getInfluencer());
      }
    }
    return ResponseEntity.ok(postRepository.findByUserIn(users, pageable));
  }

  // page, size, sort
  @GetMapping("/api/posts/search")
  public ResponseEntity searchPost(Pageable pageable, @RequestParam("search") String search) {
    return ResponseEntity.ok(postRepository.findByAttributes(search, pageable));
  }

  @GetMapping("/api/posts")
  public ResponseEntity getPost(Authentication authentication, @RequestParam("id") Long id) {
    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();
      ApplicationUser postUser = post.getUser();

      logger.info("Post USER: " + postUser + " " + checkFollowing(user, postUser));

      if (!post.getUser().isPrivateProfile() || postUser.equals(user) || checkFollowing(user, postUser)) {
        return ResponseEntity.ok(post);

      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }

  @PostMapping("/api/posts")
  public ResponseEntity newPost(Authentication authentication, @RequestBody Post newPost) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    newPost.setUser(user);
    postRepository.save(newPost);
    return handleSuccess("Saved post");
  }
  @PutMapping("/api/posts")
  public ResponseEntity editPost(Authentication authentication, @RequestParam("id") Long id, @RequestBody Post newPost) {
    Optional<Post> maybePost =  postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();

      ApplicationUser postUser = post.getUser();

      if (postUser.equals(user)) {
        post.setCaption(newPost.getCaption());
        post.setImageURL(newPost.getImageURL());
        post.setCity(newPost.getCity());
        post.setCountry(newPost.getCountry());
        postRepository.save(post);
        return handleSuccess("Edited Post");

      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }


  @DeleteMapping("/api/posts")
  public ResponseEntity deletePost(Authentication authentication, @RequestParam("id") Long id) {
    Optional<Post> maybePost =  postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();
      ApplicationUser postUser = post.getUser();

      if (postUser.equals(user)) {
        postRepository.delete(post);
        return handleSuccess("Deleted like successfully");

      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }
}
