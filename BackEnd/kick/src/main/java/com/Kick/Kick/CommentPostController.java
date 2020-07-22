package com.Kick.Kick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CommentPostController extends Controller {

  private final CommentPostRepository commentPostRepository;
  private final ApplicationUserRepository applicationUserRepository;
  private final PostRepository postRepository;

  @Autowired
  public CommentPostController(CommentPostRepository commentPostRepository, ApplicationUserRepository applicationUserRepository, PostRepository postRepository) {
    this.commentPostRepository = commentPostRepository;
    this.applicationUserRepository = applicationUserRepository;
    this.postRepository = postRepository;
  }

  @GetMapping("/api/commentPosts/{id}")
  public ResponseEntity getComment(Authentication authentication, @PathVariable @NonNull Long id) {
      Optional<CommentPost> maybeComment = commentPostRepository.findById(id);

      if (maybeComment.isPresent()) {
        ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
        CommentPost commentPost = maybeComment.get();

        if (!commentPost.getUser().isPrivateProfile() || user.equals(commentPost.getUser()) || checkFollowing(user, commentPost.getUser())) {
          return ResponseEntity.ok(commentPost);

        } else {
          return handleBadCredentials(authentication.getName());
        }

      } else {
        return handleNotFound(id.toString());
      }
  }

  @PostMapping("/api/posts/{id}/commentPosts")
  public ResponseEntity comment(Authentication authentication, @PathVariable @NonNull Long id, @RequestBody String comment) {

    Optional<Post> maybePost = postRepository.findById(id);

    if (maybePost.isPresent()) {
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
      Post post = maybePost.get();

      if (!post.getUser().isPrivateProfile() || user.equals(post.getUser()) || checkFollowing(user, post.getUser())) {
        CommentPost commentPost = new CommentPost(user, post, comment);
        commentPostRepository.save(commentPost);
        post.addComment(commentPost);
        postRepository.save(post);
        return ResponseEntity.ok(post);

      } else {
        return handleBadCredentials(authentication.getName());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }

  @PutMapping("/api/commentPosts/{id}")
  public ResponseEntity editComment(@AuthenticationPrincipal ApplicationUser user, @PathVariable @NonNull Long id, @RequestBody String comment) {
      Optional<CommentPost> maybeCommentPost =  commentPostRepository.findById(id);

      if (maybeCommentPost.isPresent()) {
        CommentPost commentPost = maybeCommentPost.get();
        ApplicationUser postUser = commentPost.getUser();

        if (postUser.equals(user)) {
          commentPost.setComment(comment);
          return ResponseEntity.ok(commentPostRepository.save(commentPost));

        } else {
          return handleBadCredentials(user.getUsername());
        }

      } else {
        return handleNotFound(id.toString());
      }
  }

  @DeleteMapping("/api/commentPosts/{id}")
  public ResponseEntity deleteComment(@AuthenticationPrincipal ApplicationUser user, @PathVariable @NonNull Long id) {
      Optional<CommentPost> maybeCommentPost =  commentPostRepository.findById(id);

      if (maybeCommentPost.isPresent()) {
        CommentPost commentPost = maybeCommentPost.get();

        if (commentPost.getUser().equals(user)) {
          commentPostRepository.delete(commentPost);
          return handleSuccess("Deleted following successfully");

        } else {
          return handleBadCredentials(user.getUsername());
        }

      } else {
        return handleNotFound(id.toString());
      }
  }

}
