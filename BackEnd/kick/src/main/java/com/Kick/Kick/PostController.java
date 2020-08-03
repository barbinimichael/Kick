package com.Kick.Kick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class PostController extends Controller {

  private final PostRepository postRepository;
  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;
  private final CommentPostRepository commentPostRepository;

  @Autowired
  public PostController(ApplicationUserRepository applicationUserRepository,
                        PostRepository postRepository, FollowingRepository followingRepository, CommentPostRepository commentPostRepository) {
    this.applicationUserRepository = applicationUserRepository;
    this.postRepository = postRepository;
    this.followingRepository = followingRepository;
    this.commentPostRepository = commentPostRepository;
  }

  @GetMapping("/api/posts/user/{username}")
  public ResponseEntity getPosts(Authentication authentication, Pageable pageable, @PathVariable @NonNull String username ) {
    ApplicationUser thisUser = applicationUserRepository.findByUsername(authentication.getName()).get();
    Optional<ApplicationUser> maybeUser = applicationUserRepository.findByUsername(username);

    if (maybeUser.isPresent()) {
      ApplicationUser user = maybeUser.get();
      Optional<Following> maybeFollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(thisUser.getUsername(), user.getUsername());

      if (thisUser.equals(user) || !user.isPrivateProfile() || (maybeFollowing.isPresent() && maybeFollowing.get().isAccepted())) {
        return ResponseEntity.ok(postRepository.findByUserOrderByTimeAsc(user, pageable));

      } else {
        return ResponseEntity.ok("Not following or public");
      }

    } else {
      return handleNotFound(username);
    }

  }

  @GetMapping("/api/posts/explore")
  public ResponseEntity getExplore(Pageable pageable) {
    ArrayList<Post> randomPosts = new ArrayList<>();
    int numEntries = pageable.getPageSize();
    long repositorySize = postRepository.count();
    for (int i = 0; i < numEntries; i++) {
      int index = (int)(Math.random() * repositorySize);
      Page<Post> post = postRepository.findAllByUserPrivateProfile(false, PageRequest.of(index, 1));
      randomPosts.addAll(post.getContent());
    }
    return ResponseEntity.ok(new PageImpl<>(randomPosts));
  }

  @GetMapping("/api/posts/feed")
  public ResponseEntity getFeed(Authentication authentication, Pageable pageable) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    ArrayList<ApplicationUser> users = new ArrayList<>();
    users.add(user); // should be in own feed
    for (Following following : user.getWhereIsFollower()) {
      if (following.isAccepted()) {
        users.add(following.getInfluencer());
      }
    }
    Page<Post> feed = postRepository.findByUserInOrderByTimeDesc(users, pageable);
    return ResponseEntity.ok(feed);
  }

  @GetMapping("/api/posts/search")
  public ResponseEntity searchPost(Pageable pageable, @RequestParam("search") String search) {
    return ResponseEntity.ok(postRepository.findByAttributes(search, pageable));
  }

  @GetMapping("/api/posts/{id}")
  public ResponseEntity getPost(Authentication authentication, @PathVariable @NonNull long id) {
    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();
      ApplicationUser postUser = post.getUser();

      if (!post.getUser().isPrivateProfile() || postUser.equals(user) || checkFollowing(user, postUser)) {
        return ResponseEntity.ok(post);

      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(String.valueOf(id));
    }
  }

  @GetMapping("/api/posts/{id}/comments")
  public ResponseEntity getPostComments(Authentication authentication, Pageable pageable, @PathVariable @NonNull long id) {
    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();
      ApplicationUser postUser = post.getUser();

      if (!post.getUser().isPrivateProfile() || postUser.equals(user) || checkFollowing(user, postUser)) {
        return ResponseEntity.ok(commentPostRepository.findAllByPost(post, pageable));

      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(String.valueOf(id));
    }
  }

  @PostMapping("/api/posts")
  public ResponseEntity newPost(Authentication authentication, @RequestBody Post newPost) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    newPost.setUser(user);
    return(ResponseEntity.ok(postRepository.save(newPost)));
  }

  @PutMapping("/api/posts/{id}")
  public ResponseEntity editPost(Authentication authentication, @PathVariable @NonNull Long id, @RequestBody Post newPost) {
    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();

      ApplicationUser postUser = post.getUser();

      if (postUser.equals(user)) {
        post.setCaption(newPost.getCaption());
        post.setImageURL(newPost.getImageURL());
        post.setCity(newPost.getCity());
        post.setCountry(newPost.getCountry());
        return(ResponseEntity.ok(postRepository.save(post)));

      } else {
        return handleBadCredentials(user.getUsername());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }


  @DeleteMapping("/api/posts/{id}")
  public ResponseEntity deletePost(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<Post> maybePost = postRepository.findById(id);

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
