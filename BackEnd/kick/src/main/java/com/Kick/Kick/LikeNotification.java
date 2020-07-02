package com.Kick.Kick;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LikeNotification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private ApplicationUser postOwner;

  @ManyToOne()
  @JoinColumn(name = "post_id")
  @JsonBackReference(value = "post-like_notification")
  private Post post;

  private long time;
  private String userLiked;
  private long thePostId;

  public LikeNotification() {

  }

  public LikeNotification(ApplicationUser postOwner, Post post, String userLiked) {
    this.postOwner = postOwner;
    this.post = post;
    this.userLiked = userLiked;
    this.thePostId = post.getId();
    this.time = Instant.now().toEpochMilli();
  }

  public ApplicationUser getPostOwner() {
    return postOwner;
  }

  public void setPostOwner(ApplicationUser postOwner) {
    this.postOwner = postOwner;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getUserLiked() {
    return userLiked;
  }

  public void setUserLiked(String userLiked) {
    this.userLiked = userLiked;
  }

  public long getThePostId() {
    return thePostId;
  }

  public void setThePostId(long thePostId) {
    this.thePostId = thePostId;
  }
}
