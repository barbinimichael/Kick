package com.Kick.Kick;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.DynamicUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

enum Gender {
  MALE("MALE"), FEMALE("FEMALE"), OTHER("OTHER");

  private final String gender;

  Gender(String gender) {
    if (gender.equals("MALE") || gender.equals("FEMALE")) {
      this.gender = gender;
    } else {
      this.gender = "OTHER";
    }
  }

  static Gender getEnum(String gender) {
    if (gender.equals("MALE")) {
      return Gender.MALE;

    } else if (gender.equals("FEMALE")) {
      return Gender.FEMALE;

    } else {
      return Gender.OTHER;
    }
  }
}

@Entity
@DynamicUpdate
public class ApplicationUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  private String firstName;

  private String lastName;

  private String email;

  private Instant birthday;

  private String city;

  private String country;

  private Gender gender;

  private String biography;

  private String profilePictureURL;

  private boolean privateProfile;

  @OneToMany(mappedBy = "user",
      orphanRemoval = true)
  @JsonManagedReference
  private Set<Post> posts;

  @OneToMany(mappedBy = "influencer",
      orphanRemoval = true)
  @JsonManagedReference(value = "following-influencer")
  private Set<Following> whereIsInfluencer;

  @OneToMany(mappedBy = "follower",
      orphanRemoval = true)
  @JsonManagedReference(value = "following-follower")
  private Set<Following> whereIsFollower;

  @OneToMany(mappedBy = "user",
      orphanRemoval = true)
  @JsonManagedReference
  private Set<LikePost> likes;

  @OneToMany(mappedBy = "user",
      orphanRemoval = true)
  @JsonManagedReference
  private Set<CommentPost> comments;

  @OneToMany(mappedBy = "postOwner",
      orphanRemoval = true)
  @JsonManagedReference
  private Set<LikeNotification> likeNotifications;

  @OneToMany(mappedBy = "influencer",
      orphanRemoval = true)
  @JsonManagedReference
  private Set<FollowingNotification> followingNotifications;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  public ApplicationUser() {
  }

  public ApplicationUser(String username,
                         String password,
                         String firstName,
                         String lastName,
                         String email,
                         String birthday,
                         String city,
                         String country,
                         String gender,
                         String biography,
                         String profilePictureURL,
                         boolean privateProfile) {
    this(username,
        password,
        firstName,
        lastName,
        email,
        LocalDate.parse(birthday).atStartOfDay(ZoneId.of("UTC")).toInstant(),
        city,
        country,
        Gender.getEnum(gender),
        biography,
        profilePictureURL,
        privateProfile);
  }

  public ApplicationUser(String username,
                         String password,
                         String firstName,
                         String lastName,
                         String email,
                         Instant birthday,
                         String city,
                         String country,
                         Gender gender,
                         String biography,
                         String profilePictureURL,
                         boolean privateProfile) {
    this(username,
        password,
        firstName,
        lastName,
        email,
        birthday,
        city,
        country,
        gender,
        biography,
        profilePictureURL,
        privateProfile,
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>());
  }

  public ApplicationUser(String username,
                         String password,
                         String firstName,
                         String lastName,
                         String email,
                         Instant birthday,
                         String city,
                         String country,
                         Gender gender,
                         String biography,
                         String profilePictureURL,
                         boolean privateProfile,
                         Set<Post> posts,
                         Set<Following> influencers,
                         Set<Following> followers,
                         Set<LikePost> likes,
                         Set<CommentPost> comments) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.birthday = birthday;
    this.city = city;
    this.country = country;
    this.gender = gender;
    this.biography = biography;
    this.profilePictureURL = profilePictureURL;
    this.privateProfile = privateProfile;
    this.setPassword(password);

    this.posts = posts;
    this.whereIsFollower = followers;
    this.whereIsInfluencer = influencers;
    this.comments = comments;
    this.likes = likes;

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Instant getBirthday() {
    return birthday;
  }

  public void setBirthday(Instant birthday) {
    this.birthday = birthday;
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

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public String getProfilePictureURL() {
    return profilePictureURL;
  }

  public void setProfilePictureURL(String profilePictureURL) {
    this.profilePictureURL = profilePictureURL;
  }

  public boolean isPrivateProfile() {
    return privateProfile;
  }

  public void setPrivateProfile(boolean privateProfile) {
    this.privateProfile = privateProfile;
  }

  public Set<com.Kick.Kick.Post> getPosts() {
    return posts;
  }

  public void setPosts(Set<com.Kick.Kick.Post> posts) {
    if (posts == null) {
      this.posts = new HashSet<>();
    } else {
      this.posts = posts;
      this.posts.forEach(x -> x.setUser(this));
    }
  }

  public void addPost(com.Kick.Kick.Post post) {
    this.posts.add(post);
    post.setUser(this);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<Following> getWhereIsInfluencer() {
    return this.whereIsInfluencer;
  }

  public void setWhereIsInfluencer(Set<Following> whereIsInfluencer) {
    if (whereIsInfluencer == null) {
      this.whereIsInfluencer = new HashSet<>();
    } else {
      this.whereIsInfluencer = whereIsInfluencer;
    }
  }

  public void addWhereIsInfluencer(Following influencer) {
    this.whereIsInfluencer.add(influencer);
  }

  public Set<Following> getWhereIsFollower() {
    return whereIsFollower;
  }

  public void setWhereIsFollower(Set<Following> whereIsFollower) {
    if (whereIsFollower == null) {
      this.whereIsFollower = new HashSet<>();
    } else {
      this.whereIsFollower = whereIsFollower;
    }
  }

  public void addWhereIsFollower(Following follower) {
    this.whereIsFollower.add(follower);
  }

  public Set<LikePost> getLikes() {
    return likes;
  }

  public void setLikes(Set<LikePost> likes) {
    if (likes == null) {
      this.likes = new HashSet<>();
    } else {
      this.likes = likes;
    }
  }

  public void addLike(LikePost like) {
    this.likes.add(like);
  }

  public Set<CommentPost> getComments() {
    return comments;
  }

  public void setComments(Set<CommentPost> comments) {
    if (comments == null) {
      this.comments = new HashSet<>();
    } else {
      this.comments = comments;
    }
  }

  public void addComment(CommentPost comment) {
    this.comments.add(comment);
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

  public Set<FollowingNotification> getFollowingNotifications() {
    return followingNotifications;
  }

  public void setFollowingNotifications(Set<FollowingNotification> followingNotifications) {
    this.followingNotifications = followingNotifications;
  }

  public void addFollowingNotification(FollowingNotification followingNotification) {
    this.followingNotifications.add(followingNotification);
  }

  public ApplicationUser generatePubliclyVisibleUser() {
    if (this.isPrivateProfile()) {
      return new ApplicationUser(
          this.username,
          "",
          this.firstName,
          this.lastName,
          "",
          "0001-01-01",
          this.city,
          this.country,
          "",
          this.biography,
          this.profilePictureURL,
          true);
    } else {
      return this.generateVisibleUser();
    }
  }

  public ApplicationUser generateVisibleUser() {
    return new ApplicationUser(
        this.username,
        "",
        this.firstName,
        this.lastName,
        this.email,
        this.birthday,
        this.city,
        this.country,
        this.gender,
        this.biography,
        this.profilePictureURL,
        this.privateProfile,
        this.posts,
        this.whereIsInfluencer,
        this.whereIsFollower,
        this.likes,
        this.comments);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ApplicationUser that = (ApplicationUser) o;
    return privateProfile == that.privateProfile &&
        Objects.equals(id, that.id) &&
        Objects.equals(username, that.username) &&
        Objects.equals(password, that.password) &&
        Objects.equals(firstName, that.firstName) &&
        Objects.equals(lastName, that.lastName) &&
        Objects.equals(email, that.email) &&
        Objects.equals(birthday, that.birthday) &&
        Objects.equals(city, that.city) &&
        Objects.equals(country, that.country) &&
        gender == that.gender &&
        Objects.equals(biography, that.biography) &&
        Objects.equals(profilePictureURL, that.profilePictureURL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, firstName, lastName, email, birthday, city, country, gender, biography, profilePictureURL, privateProfile);
  }

  @Override
  public String toString() {
    return "ApplicationUser{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", birthday=" + birthday +
        ", city='" + city + '\'' +
        ", country='" + country + '\'' +
        ", gender=" + gender +
        ", biography='" + biography + '\'' +
        ", profilePictureURL='" + profilePictureURL + '\'' +
        ", privateProfile=" + privateProfile + '}';
  }
}
