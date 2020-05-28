package com.Kick.Kick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashSet;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FollowingUnitTest {

  private final FollowingController followingController;
  private final ApplicationUserController applicationUserController;
  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;
  private final PostRepository postRepository;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  private ApplicationUser m;
  private ApplicationUser mTwo;
  private ApplicationUser mThree;

  private Post p, pTwo, pThree, pFour, pFive;

  private Following mFollowMTwo, mFollowMThree, mTwoFollowM, mTwoFollowMThree;

  @Autowired
  FollowingUnitTest(FollowingController followingController, ApplicationUserController applicationUserController, ApplicationUserRepository applicationUserRepository,
                    FollowingRepository followingRepository, PostRepository postRepository) {
    this.followingController = followingController;
    this.applicationUserController = applicationUserController;
    this.applicationUserRepository = applicationUserRepository;
    this.followingRepository = followingRepository;
    this.postRepository = postRepository;
  }

  @BeforeEach
  void intiEntities() {
    this.applicationUserRepository.deleteAll();

    m = new ApplicationUser("appTestOne",
        "password",
        "Mike",
        "B",
        "mbarbzzz@jmail.com",
        "2020-05-10",
        "Boston",
        "USA",
        "MALE",
        "Hi",
        "https://imgur.com/a/qKEjLCD",
        true);

    mTwo = new ApplicationUser("appTestTwo",
        "secure password",
        "Mikey",
        "Boz",
        "ye2@me.you",
        "2020-10-11",
        "somewhere",
        "eartch",
        "MALE",
        "Heyo", "image.im",
        true);

    mThree = new ApplicationUser("appTestThree",
        "secure password",
        "Mikey II",
        "Boz II",
        "ye3@me.you",
        "2020-10-12",
        "somewhere else",
        "earth correct",
        "MALE",
        "Heyo ye", "image.img",
        false);

    p = new Post("Hi",
        "www.link.le",
        "Boston",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>());

    pTwo = new Post("Hi again",
        "www.link2.le",
        "Hawaii",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>());

    pThree = new Post("Ye Im moving to Boston",
        "www.link3.le",
        "LA",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>());

    pFour = new Post("Boston wack",
        "www.link4.le",
        "LA",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>());

    pFive = new Post("Yawn",
        "www.link4.le",
        "Boston",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>());

    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    p.setUser(m);
    pTwo.setUser(mTwo);
    pThree.setUser(mThree);
    pFour.setUser(mThree);
    pFive.setUser(mThree);

    postRepository.save(p);
    postRepository.save(pTwo);
    postRepository.save(pThree);
    postRepository.save(pFour);
    postRepository.save(pFive);

    mFollowMTwo = new Following(m, mTwo);
    mTwoFollowM = new Following(mTwo, m);
    mFollowMThree = new Following(m, mThree);
    mTwoFollowMThree = new Following(mTwo, mThree);
  }

  @Test
  @Transactional
  // test that user who follows can access user information
  void getUserFromUsername() {
    followingController.follow(new MockAuthentication(mTwo), m.getUsername());
    followingController.follow(new MockAuthentication(mThree), m.getUsername());
    Following following = followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), m.getUsername()).get();
    followingController.acceptFollower(new MockAuthentication(m),following.getId(), true);

    ResponseEntity responseSameTwo =
        applicationUserController.getApplicationUserFromUsername(new MockAuthentication(m), m.getUsername());
    ResponseEntity responseFollower =
        applicationUserController.getApplicationUserFromUsername(new MockAuthentication(mTwo), m.getUsername());
    ResponseEntity responseStillPrivate =
        applicationUserController.getApplicationUserFromUsername(new MockAuthentication(mThree), m.getUsername());

    assertEquals(m, responseSameTwo.getBody());
    assertTrue(applicationUserController.checkFollowing(mTwo, m));
    assertEquals(m.generatePublicUser(), responseFollower.getBody());
    assertEquals(m.generatePrivateUser(), responseStillPrivate.getBody());
  }

  @Test
  @Transactional
    // test that user who follows can access user information
  void getUserFromId() {
    followingController.follow(new MockAuthentication(mTwo), m.getUsername());
    followingController.follow(new MockAuthentication(mThree), m.getUsername());
    Following following = followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), m.getUsername()).get();
    followingController.acceptFollower(new MockAuthentication(m),following.getId(), true);

    ResponseEntity responseSameTwo =
        applicationUserController.getApplicationUserFromId(new MockAuthentication(m), m.getId());
    ResponseEntity responseFollower =
        applicationUserController.getApplicationUserFromId(new MockAuthentication(mTwo), m.getId());
    ResponseEntity responseStillPrivate =
        applicationUserController.getApplicationUserFromId(new MockAuthentication(mThree), m.getId());

    assertEquals(m, responseSameTwo.getBody());
    assertTrue(applicationUserController.checkFollowing(mTwo, m));
    assertEquals(m.generatePublicUser(), responseFollower.getBody());
    assertEquals(m.generatePrivateUser(), responseStillPrivate.getBody());
  }

  @Test
  @Transactional
  void testNewFollowing() {
    followingRepository.save(mTwoFollowMThree);
    assertEquals(mTwoFollowMThree, followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), mThree.getUsername()).get());

    ResponseEntity mFollowMTwoResponse = followingController.follow(new MockAuthentication(m), mTwo.getUsername());
    assertEquals(ResponseEntity.status(HttpStatus.OK).body("Saved following"), mFollowMTwoResponse);
    assertEquals(mFollowMTwo, followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get());

    ResponseEntity mTwoFollowMResponse = followingController.follow(new MockAuthentication(mTwo), m.getUsername());
    assertEquals(ResponseEntity.status(HttpStatus.OK).body("Saved following"), mTwoFollowMResponse);
    assertEquals(mTwoFollowM, followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), m.getUsername()).get());

    ResponseEntity mFollowMThreeResponse = followingController.follow(new MockAuthentication(m), mThree.getUsername());
    assertEquals(ResponseEntity.status(HttpStatus.OK).body("Saved following"), mFollowMThreeResponse);
    assertEquals(mFollowMThree, followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mThree.getUsername()).get());

    ResponseEntity sameResponse = followingController.follow(new MockAuthentication(m), mThree.getUsername());
    assertEquals(ResponseEntity.status(HttpStatus.OK).body("Saved following"), sameResponse);
    assertEquals(mFollowMThree, followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mThree.getUsername()).get());
  }

  @Test
  @Transactional
  void testGetFollowing() {
    followingController.follow(new MockAuthentication(m), mTwo.getUsername());
    Following following = followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get();
    assertEquals(HttpStatus.OK, followingController.getFollowing(new MockAuthentication(m), following.getId()).getStatusCode());
    assertEquals(following, followingController.getFollowing(new MockAuthentication(m), following.getId()).getBody());

    ApplicationUser mFollowing = applicationUserRepository.findByUsername(m.getUsername()).get();
    assertTrue(mFollowing.getWhereIsInfluencer().contains(following));

    ApplicationUser mTwoFollowing = applicationUserRepository.findByUsername(mTwo.getUsername()).get();
    assertTrue(mTwoFollowing.getWhereIsFollower().contains(following));
  }

  @Test
  @Transactional
  void testAcceptFollowing() {
    followingController.follow(new MockAuthentication(m), mTwo.getUsername());
    Following following = followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get();

    ResponseEntity rejectedResponse = followingController.acceptFollower(new MockAuthentication(m), following.getId(), true);
    Following deniedFollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get();
    assertEquals(HttpStatus.FORBIDDEN, rejectedResponse.getStatusCode());
    assertFalse(deniedFollowing.isAccepted());

    ResponseEntity acceptResponse = followingController.acceptFollower(new MockAuthentication(mTwo), following.getId(), true);
    Following acceptfollowing = followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get();
    assertEquals(HttpStatus.OK, acceptResponse.getStatusCode());
    assertTrue(acceptfollowing.isAccepted());
  }

  @Test
  @Transactional
  void testDeletePost() {
    followingController.follow(new MockAuthentication(m), mTwo.getUsername());
    followingController.follow(new MockAuthentication(mTwo), mThree.getUsername());

    Following followingOne = followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get();
    logger.info(followingOne.getId().toString());

    ResponseEntity deleteFollowing = followingController.deleteFollowing(new MockAuthentication(mTwo), followingOne.getId());
    assertEquals(HttpStatus.OK, deleteFollowing.getStatusCode());
    assertTrue(postRepository.findById(followingOne.getId()).isEmpty());

    ResponseEntity notFoundResponse = followingController.deleteFollowing(new MockAuthentication(mTwo), (long) 1000);
    assertEquals(HttpStatus.NOT_FOUND, notFoundResponse.getStatusCode());

    Following followingTwo = followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), mThree.getUsername()).get();
    logger.info(followingTwo.getId().toString());
    ResponseEntity badResponse = followingController.deleteFollowing(new MockAuthentication(m), followingTwo.getId());
    assertEquals(HttpStatus.FORBIDDEN, badResponse.getStatusCode());
    assertTrue(followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), mThree.getUsername()).isPresent());

    ResponseEntity deleteInfluencer = followingController.deleteFollowing(new MockAuthentication(mTwo), followingTwo.getId());
    assertEquals(HttpStatus.OK, deleteInfluencer.getStatusCode());
    assertTrue(postRepository.findById(followingTwo.getId()).isEmpty());
    assertTrue(followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), mThree.getUsername()).isEmpty());
  }

}
