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

@SpringBootTest
public class CommentPostUnitTest {

  private final FollowingController followingController;
  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;
  private final PostRepository postRepository;
  private final CommentPostRepository commentPostRepository;
  private final CommentPostController commentPostController;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  private ApplicationUser m;
  private ApplicationUser mTwo;
  private ApplicationUser mThree;

  private Post p, pTwo, pThree, pFour, pFive;

  private Following mFollowMTwo, mFollowMThree, mTwoFollowM, mTwoFollowMThree;

  private CommentPost c, cTwo, cThree;

  private String cComment, cTwoComment, cThreeComment;

  @Autowired
  CommentPostUnitTest(FollowingController followingController, ApplicationUserRepository applicationUserRepository,
                      FollowingRepository followingRepository, PostRepository postRepository, CommentPostRepository commentPostRepository, CommentPostController commentPostController) {
    this.followingController = followingController;
    this.commentPostRepository = commentPostRepository;
    this.commentPostController = commentPostController;
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

    cComment = "my own post";
    c = new CommentPost(m, p, cComment);

    cTwoComment = "other post";
    cTwo = new CommentPost(mTwo, p, cTwoComment);
  }

  @Test
  @Transactional
  void testCommentPost() {
    followingController.follow(new MockAuthentication(mTwo), m.getUsername());
    followingController.follow(new MockAuthentication(m), mTwo.getUsername());
    Following fOne = followingRepository.findByFollowerUsernameAndInfluencerUsername(m.getUsername(), mTwo.getUsername()).get();
    Following fTwo = followingRepository.findByFollowerUsernameAndInfluencerUsername(mTwo.getUsername(), m.getUsername()).get();
    followingController.acceptFollower(new MockAuthentication(m), fOne.getId(), true);
    followingController.acceptFollower(new MockAuthentication(m), fTwo.getId(), true);

    ResponseEntity commentResponse = commentPostController.comment(new MockAuthentication(m), p.getId(), cComment);
    assertEquals(HttpStatus.OK, commentResponse.getStatusCode());

    ResponseEntity illegalResponse = commentPostController.comment(new MockAuthentication(mThree), p.getId(), "illegal");
    assertEquals(HttpStatus.FORBIDDEN, illegalResponse.getStatusCode());

    CommentPost commentPost = commentPostRepository.findByUserAndPostAndComment(m, p, cComment).get();
    assertEquals(c, commentPost);

    ResponseEntity sameGetResponse = commentPostController.getComment(new MockAuthentication(m), commentPost.getId());
    assertEquals(c, sameGetResponse.getBody());
    assertEquals(HttpStatus.OK, sameGetResponse.getStatusCode());

    ResponseEntity posterGetResponse = commentPostController.getComment(new MockAuthentication(mTwo), commentPost.getId());
    assertEquals(c, posterGetResponse.getBody());
    assertEquals(HttpStatus.OK, posterGetResponse.getStatusCode());
  }
}
