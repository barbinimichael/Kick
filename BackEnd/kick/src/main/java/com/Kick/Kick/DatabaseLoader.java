package com.Kick.Kick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

/**
 * Responsible for loading example data into the database.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

  private final ApplicationUserRepository applicationUserRepository;
  private final PostRepository postRepository;
  private final FollowingRepository followingRepository;
  private final LikePostRepository likePostRepository;
  private final CommentPostRepository commentPostRepository;
  private final ApplicationUserController applicationUserController;
  private final FollowingController followingController;
  private final PostController postController;
  private final LikePostController likePostController;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private ArrayList<ApplicationUser> users = new ArrayList<>();
  private ApplicationUser m;
  private Post pThree;

  @Autowired
  public DatabaseLoader(ApplicationUserRepository applicationUserRepository,
                        PostRepository postRepository,
                        FollowingRepository followingRepository,
                        LikePostRepository likePostRepository, CommentPostRepository commentPostRepository, ApplicationUserController applicationUserController, FollowingController followingController, PostController postController, LikePostController likePostController, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.postRepository = postRepository;
    this.followingRepository = followingRepository;
    this.likePostRepository = likePostRepository;
    this.commentPostRepository = commentPostRepository;
    this.applicationUserController = applicationUserController;
    this.followingController = followingController;
    this.postController = postController;
    this.likePostController = likePostController;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  @Transactional
  public void run(String... strings) throws Exception {

    m = new ApplicationUser("mbarbzzz",
        "password",
        "Mike",
        "B",
        "mbarbzzz@jmail.com",
        Instant.now(),
        "Boston",
        "USA",
        Gender.MALE,
        "Hi", "https://imgur.com/a/qKEjLCD",
        false);

    ApplicationUser mTwo = new ApplicationUser("mikey",
        "secure password",
        "Mikey",
        "Boz",
        "ye@me.you",
        Instant.now(),
        "somewhere",
        "eartch",
        Gender.MALE,
        "Heyo", "image.im",
        true);

    Post p = new Post("Hi",
        "www.link.le",
        "Boston",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>());

    Post pTwo = new Post("Hi again",
        "www.link2.le",
        "Boston",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>());

    pThree = new Post("mbarbzzz first Post",
        "www.link.le",
        "Boston",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>());

    applicationUserController.signUp(m);
    applicationUserController.signUp(mTwo);

    followingController.follow(new MockAuthentication(m), mTwo.getUsername());
    followingController.acceptFollower(new MockAuthentication(mTwo), followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get().getId(), true);

    postController.newPost(new MockAuthentication(mTwo), p);
    postController.newPost(new MockAuthentication(mTwo), pTwo);
    postController.newPost(new MockAuthentication(m), pThree);

    users.add(m);

    for (int i = 0; i < 100; i++) {
      createRandomUser();
    }
  }

  void createRandomUser() {
    ApplicationUser newUser = new ApplicationUser(
        generateRandomString("username:"),
        generateRandomString("password:"),
        generateRandomString("firstName:"),
        generateRandomString("lastName:"),
        generateRandomString("email:"),
        Instant.now(),
        generateRandomString("city:"),
        generateRandomString("country:"),
        Gender.OTHER,
        generateRandomString("biography:"),
        generateRandomString("profilePic:"),
        generateRandomBoolean()
    );
    applicationUserController.signUp(newUser);

    Post p = new Post(generateRandomString("caption:"),
        generateRandomString("imageURL:"),
        generateRandomString("city:"),
        generateRandomString("country:"),
        "2020-01-01",
        newUser);

    postController.newPost(new MockAuthentication(newUser), p);

    followingController.follow(new MockAuthentication(m), newUser.getUsername());
    followingController.acceptFollower(new MockAuthentication(newUser),
        followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), newUser.getUsername()).get().getId(), true);

    followingController.follow(new MockAuthentication(newUser), m.getUsername());
    followingController.acceptFollower(new MockAuthentication(m),
        followingRepository.findByFollowerUsernameAndInfluencerUsername(newUser.getUsername(), m.getUsername()).get().getId(), true);

    likePostController.like(new MockAuthentication(newUser), pThree.getId());

    users.add(newUser);
  }

  String generateRandomString(String starter) {
    StringBuilder randomString = new StringBuilder(starter);
    Random r = new Random();
    for (int i = 0; i < r.nextInt(3) + 4; i++) {
      randomString.append((char) (r.nextInt(26) + 'a'));
    }
    return randomString.toString();
  }

  boolean generateRandomBoolean() {
    Random r = new Random();
    return r.nextBoolean();
  }
}

class MockAuthentication implements Authentication {

  private String name;

  public MockAuthentication(ApplicationUser user) {
    this.name = user.getUsername();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getDetails() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public boolean isAuthenticated() {
    return true;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

  }

  @Override
  public String getName() {
    return name;
  }
}
