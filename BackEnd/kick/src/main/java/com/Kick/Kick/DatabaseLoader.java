package com.Kick.Kick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
  private final ApplicationUserController applicationUserController;
  private final FollowingController followingController;
  private final PostController postController;
  private final LikeNotificationRepository likeNotificationRepository;

  private ArrayList<ApplicationUser> users = new ArrayList<>();
  private ApplicationUser m;
  private Post pThree;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  @Autowired
  public DatabaseLoader(ApplicationUserRepository applicationUserRepository,
                        PostRepository postRepository,
                        FollowingRepository followingRepository,
                        LikePostRepository likePostRepository,
                        ApplicationUserController applicationUserController,
                        FollowingController followingController,
                        PostController postController, LikeNotificationRepository likeNotificationRepository) {
    this.applicationUserRepository = applicationUserRepository;
    this.postRepository = postRepository;
    this.followingRepository = followingRepository;
    this.likePostRepository = likePostRepository;
    this.applicationUserController = applicationUserController;
    this.followingController = followingController;
    this.postController = postController;
    this.likeNotificationRepository = likeNotificationRepository;
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
        true);

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

    Post newPost = new Post(generateRandomString("caption:"),
        generateRandomString("imageURL:"),
        generateRandomString("city:"),
        generateRandomString("country:"),
        "2020-01-01",
        newUser);

    postController.newPost(new MockAuthentication(newUser), newPost);

    followingController.follow(new MockAuthentication(m), newUser.getUsername());

    // artificially insert accepted following since cannot do multiple transactions in a row before start
     if (generateRandomBoolean()) {
       Following newFollowing = new Following(newUser, m);
       newFollowing.setAccepted(true);
       m.addWhereIsInfluencer(newFollowing);
       newUser.addWhereIsFollower(newFollowing);

       LikePost newLike = new LikePost(newUser, pThree);
       pThree.addLike(newLike);

       LikeNotification newLikeNotification = new LikeNotification(m, pThree, newUser.getUsername());

       likeNotificationRepository.save(newLikeNotification);
       likePostRepository.save(newLike);
       postRepository.save(pThree);
       followingRepository.save(newFollowing);
       applicationUserRepository.save(m);
       applicationUserRepository.save(newUser);
     } else {
       followingController.follow(new MockAuthentication(newUser), m.getUsername());
     }

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
