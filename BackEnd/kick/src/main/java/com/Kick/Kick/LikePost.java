package com.Kick.Kick;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class LikePost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private ApplicationUser user;

  @ManyToOne()
  @JoinColumn(name = "post_id")
  @JsonBackReference(value="post-like_post")
  private Post post;

  public LikePost() {}

  public LikePost(ApplicationUser user, Post post) {
    this.user = user;
    this.post = post;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ApplicationUser getUser() {
    return user;
  }

  public void setUser(ApplicationUser user) {
    this.user = user;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LikePost like = (LikePost) o;
    return Objects.equals(user.getUsername(), like.user.getUsername()) &&
        Objects.equals(post, like.post);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user.getUsername(), post);
  }

  @Override
  public String toString() {
    return "Like{" +
        "applicationUser=" + user.getUsername() +
        ", post=" + post +
        '}';
  }
}
