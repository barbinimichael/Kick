package com.Kick.Kick;

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
import java.util.List;
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

  int numRandomGeneratedUsers = 100;
  List<String> randomFirstNames;
  List<String> randomLastNames;
  List<String> randomCities;
  List<String> randomCountries;
  List<String> randomBiographies;

  private ArrayList<ApplicationUser> users = new ArrayList<>();
  private ApplicationUser m;
  private Post pThree;

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

    LoadData loadData = new LoadData();

    randomFirstNames = loadData.getFirstNames(numRandomGeneratedUsers);
    randomLastNames = loadData.getLastNames(numRandomGeneratedUsers);
    randomCities = loadData.getCities(numRandomGeneratedUsers);
    randomCountries = loadData.getCountries(numRandomGeneratedUsers);
    randomBiographies = loadData.getBiographies(numRandomGeneratedUsers);

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

    for (int i = 0; i < numRandomGeneratedUsers; i++) {
      createRandomUser(randomFirstNames.get(i), randomLastNames.get(i), randomCities.get(i), randomCountries.get(i), randomBiographies.get(i));
    }
  }

  void createRandomUser(String firstName, String lastName, String city, String country, String biography) {
    ApplicationUser newUser = new ApplicationUser(
        firstName + "_" + new Random().nextInt(10000),
        generateRandomString("!.1"),
        firstName,
        lastName,
        firstName + lastName + "_" + new Random().nextInt(10000) + "@jmail.com",
        Instant.ofEpochSecond(new Random().nextInt()),
        city,
        country,
        Gender.OTHER,
        biography,
        generateRandomString("profilePic:"),
        generateRandomBoolean()
    );
    applicationUserController.signUp(newUser);

    Post newPost = new Post(biography,
        generateRandomString("imageURL:"),
        city,
        country,
        Instant.ofEpochSecond(new Random().nextInt()),
        newUser);

    // postRepository.save(newPost);
    postController.newPost(new MockAuthentication(newUser), newPost);
    // followingRepository.save(new Following(m, newUser));
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
      // followingRepository.save(new Following(newUser, m));
      followingRepository.save(new Following(newUser, m));
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
