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
public class CommentPost {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private ApplicationUser user;

  private String username;

  @ManyToOne()
  @JoinColumn(name = "post_id")
  @JsonBackReference(value="post-comment_post")
  private Post post;

  private String comment;

  public CommentPost() {}

  public CommentPost(ApplicationUser user,
                     Post post,
                     String comment) {
    this.user = user;
    this.post = post;
    this.comment = comment;
    this.username = user.getUsername();
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

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommentPost that = (CommentPost) o;
    return Objects.equals(user.getUsername(), that.user.getUsername()) &&
        Objects.equals(post.getId(), that.post.getId()) &&
        Objects.equals(comment, that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user.getUsername(), post.getId(), comment);
  }

  @Override
  public String toString() {
    return "CommentPost{" +
        "id=" + id +
        ", user=" + user.getUsername() +
        ", post=" + post.getId() +
        ", comment='" + comment + '\'' +
        '}';
  }
}
