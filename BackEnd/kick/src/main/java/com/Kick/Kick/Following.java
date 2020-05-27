package com.Kick.Kick;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Following {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne()
  @JoinColumn(name = "influencer_id")
  @JsonBackReference(value="following-influencer")
  private ApplicationUser influencer;

  @ManyToOne()
  @JoinColumn(name = "follower_id")
  @JsonBackReference(value="following-follower")
  private ApplicationUser follower;

  private boolean accepted;

  public Following() {}

  public Following(ApplicationUser follower, ApplicationUser influencer) {
    this.influencer = influencer;
    this.follower = follower;
    this.accepted = false;
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

  public ApplicationUser getFollower() {
    return follower;
  }

  public void setFollower(ApplicationUser follower) {
    this.follower = follower;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Following following = (Following) o;
    return accepted == following.accepted &&
        Objects.equals(influencer.getUsername(), following.influencer.getUsername()) &&
        Objects.equals(follower.getUsername(), following.follower.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(influencer.getUsername(), follower.getUsername(), accepted);
  }

  @Override
  public String toString() {
    return "Following{" +
        "id=" + id +
        ", influencer=" + influencer.getUsername() +
        ", follower=" + follower.getUsername() +
        ", accepted=" + accepted +
        '}';
  }
}
