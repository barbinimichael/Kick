package com.Kick.Kick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashSet;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LikePostUnitTest {

  private final FollowingController followingController;
  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;
  private final PostRepository postRepository;
  private final LikePostController likePostController;
  private final LikePostRepository likePostRepository;

  private ApplicationUser m;
  private ApplicationUser mTwo;
  private ApplicationUser mThree;

  private Post p, pTwo, pThree, pFour, pFive;

  private Following mFollowMTwo, mFollowMThree, mTwoFollowM, mTwoFollowMThree;

  private LikePost l, lTwo, lThree, lFour;

  @Autowired
  LikePostUnitTest(FollowingController followingController, ApplicationUserRepository applicationUserRepository,
                   FollowingRepository followingRepository, PostRepository postRepository, LikePostController likePostController, LikePostRepository likePostRepository) {
    this.followingController = followingController;
    this.applicationUserRepository = applicationUserRepository;
    this.followingRepository = followingRepository;
    this.postRepository = postRepository;
    this.likePostController = likePostController;
    this.likePostRepository = likePostRepository;
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
        new HashSet<>(),
        new HashSet<>());

    pTwo = new Post("Hi again",
        "www.link2.le",
        "Hawaii",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>());

    pThree = new Post("Ye Im moving to Boston",
        "www.link3.le",
        "LA",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>());

    pFour = new Post("Boston wack",
        "www.link4.le",
        "LA",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>(),
        new HashSet<>());

    pFive = new Post("Yawn",
        "www.link4.le",
        "Boston",
        "USA",
        Instant.now(),
        new HashSet<>(),
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

    l = new LikePost(m, p);
    lTwo = new LikePost(mTwo, p);
    lThree = new LikePost(mTwo, pTwo);
    lFour = new LikePost(mThree, pThree);
  }

  @Test
  @Transactional
  void testLikePost() {
    likePostController.like(new MockAuthentication(m), p.getId());
    LikePost likePost = likePostRepository.findByUserAndPost(m, p).get();
    ResponseEntity getLikeResponse = likePostController.getLikePost(new MockAuthentication(m), likePost.getId());
    assertEquals(l, getLikeResponse.getBody());

    ResponseEntity illegalLikeResponse = likePostController.getLikePost(new MockAuthentication(mTwo), likePost.getId());
    assertEquals(HttpStatus.FORBIDDEN, illegalLikeResponse.getStatusCode());

    ResponseEntity illegalDeleteResponse = likePostController.deleteLike(new MockAuthentication(mTwo), likePost.getId());
    assertEquals(HttpStatus.FORBIDDEN, illegalDeleteResponse.getStatusCode());

    followingController.follow(new MockAuthentication(mTwo), m.getUsername());
    Following following = followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), m.getUsername()).get();
    followingController.acceptFollower(new MockAuthentication(m), following.getId(), true);

    ResponseEntity illegalDeleteFollowResponse = likePostController.deleteLike(new MockAuthentication(mTwo), likePost.getId());
    assertEquals(HttpStatus.FORBIDDEN, illegalDeleteFollowResponse.getStatusCode());

    ResponseEntity deleteResponse = likePostController.deleteLike(new MockAuthentication(m), likePost.getId());
    assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    assertTrue(likePostRepository.findByUserAndPost(m, p).isEmpty());
  }
}
