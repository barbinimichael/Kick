package com.Kick.Kick;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Post {

  private @Id
  @GeneratedValue
  Long id;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private ApplicationUser user;

  @OneToMany(mappedBy = "post",
      orphanRemoval = true)
  @JsonManagedReference(value = "post-like_post")
  private Set<LikePost> likes;

  @OneToMany(mappedBy = "post",
      orphanRemoval = true)
  @JsonManagedReference(value = "post-comment_post")
  private Set<CommentPost> comments;

  @OneToMany(mappedBy = "post",
      orphanRemoval = true)
  @JsonManagedReference(value = "post-like_notification")
  private Set<LikeNotification> likeNotifications;

  private String username;
  private String caption;
  private String imageURL;
  private String city;
  private String country;
  private Instant postDate;
  private long time;

  public Post() {
  }

  public Post(String caption,
              String imageURL,
              String city,
              String country,
              String postDate) {
    this(caption,
        imageURL,
        city,
        country,
        postDate,
        null);
  }

  public Post(String caption,
              String imageURL,
              String city,
              String country,
              String postDate,
              ApplicationUser user) {
    this(caption,
        imageURL,
        city,
        country,
        LocalDate.parse(
            postDate).atStartOfDay(ZoneId.of("UTC")).toInstant(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>(),
        user);
  }

  public Post(String caption,
              String imageURL,
              String city,
              String country,
              Instant postDate,
              Set<LikePost> likes,
              Set<CommentPost> comments,
              Set<LikeNotification> likeNotifications) {
    this(caption,
        imageURL,
        city,
        country,
        postDate,
        likes,
        comments,
        likeNotifications,
        null);
  }

  public Post(String caption,
              String imageURL,
              String city,
              String country,
              Instant postDate,
              Set<LikePost> likes,
              Set<CommentPost> comments,
              Set<LikeNotification> likeNotifications,
              ApplicationUser user) {
    this.caption = caption;
    this.imageURL = imageURL;
    this.city = city;
    this.country = country;
    this.postDate = postDate;
    this.time = postDate.toEpochMilli();

    this.likes = likes;
    this.comments = comments;

    this.user = user;
    this.likeNotifications = likeNotifications;
    if (user != null) {
      this.username = user.getUsername();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Instant getPostDate() {
    return postDate;
  }

  public void setPostDate(Instant postDate) {
    this.postDate = postDate;
  }

  public ApplicationUser getUser() {
    return user;
  }

  public void setUser(ApplicationUser user) {
    this.user = user;
    this.username = user.getUsername();
  }

  public Set<LikePost> getLikes() {
    return likes;
  }

  public void setLikes(Set<LikePost> likes) {
    this.likes = likes;
  }

  public void addLike(LikePost like) {
    this.likes.add(like);
  }

  public Set<CommentPost> getComments() {
    return comments;
  }

  public void setComments(Set<CommentPost> comments) {
    this.comments = comments;
  }

  public void addComment(CommentPost comment) {
    this.comments.add(comment);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public Set<LikeNotification> getLikeNotifications() {
    return likeNotifications;
  }

  public void setLikeNotifications(Set<LikeNotification> likeNotifications) {
    this.likeNotifications = likeNotifications;
  }

  public void addLikeNotification(LikeNotification likeNotification) {
    this.likeNotifications.add(likeNotification);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Post post = (Post) o;
    return Objects.equals(id, post.id) &&
        Objects.equals(caption, post.caption) &&
        Objects.equals(imageURL, post.imageURL) &&
        Objects.equals(city, post.city) &&
        Objects.equals(country, post.country) &&
        Objects.equals(postDate, post.postDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, caption, imageURL, city, country, postDate);
  }

  @Override
  public String toString() {
    return "Post{" +
        "id=" + id +
        ", user=" + username + '\'' +
        ", caption='" + caption + '\'' +
        ", imageURL='" + imageURL + '\'' +
        ", city='" + city + '\'' +
        ", country='" + country + '\'' +
        ", postDate=" + postDate +
        '}';
  }
}
