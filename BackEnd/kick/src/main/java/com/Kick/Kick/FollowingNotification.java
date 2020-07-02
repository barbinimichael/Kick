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
public class FollowingNotification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private ApplicationUser influencer;

  private long time;
  private long theFollowingId;

  public FollowingNotification() {

  }

  public FollowingNotification(ApplicationUser influencer, long theFollowingId) {
    this.influencer = influencer;
    this.theFollowingId = theFollowingId;

    this.time = Instant.now().toEpochMilli();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ApplicationUser getInfluencer() {
    return influencer;
  }

  public void setInfluencer(ApplicationUser influencer) {
    this.influencer = influencer;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public long getTheFollowingId() {
    return theFollowingId;
  }

  public void setTheFollowingId(long theFollowingId) {
    this.theFollowingId = theFollowingId;
  }
}
