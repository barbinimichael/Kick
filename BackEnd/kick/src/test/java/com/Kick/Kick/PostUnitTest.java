package com.Kick.Kick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PostUnitTest {

  private final PostController postController;
  private final PostRepository postRepository;
  private final FollowingRepository followingRepository;
  private final ApplicationUserRepository applicationUserRepository;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  private ApplicationUser m;
  private ApplicationUser mTwo;
  private ApplicationUser mThree;
  private ApplicationUser mFour;

  private Post p;
  private Post pTwo;
  private Post pThree;
  private Post pFour;
  private Post pFive;

  private Following following;
  private Following followingTwo;

  @Autowired
  PostUnitTest(PostController postController, PostRepository postRepository, FollowingRepository followingRepository, ApplicationUserRepository applicationUserRepository) {
    this.postController = postController;
    this.postRepository = postRepository;
    this.followingRepository = followingRepository;
    this.applicationUserRepository = applicationUserRepository;
  }

  @BeforeEach
  void intiEntities() {
    this.applicationUserRepository.deleteAll();
    this.postRepository.deleteAll();

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
        "Miker",
        "Boss",
        "ye3@me.you",
        "2020-10-11",
        "b",
        "america",
        "MALE",
        "Ye", "image3.im",
        false);

    mFour = new ApplicationUser("mFour",
        "secure password",
        "M",
        "Bossy",
        "ye4@me.you",
        "2020-02-11",
        "Boston",
        "america",
        "MALE",
        "Ye", "image4.im",
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

    following = new Following(m, mTwo);
    following.setAccepted(true);
    followingTwo = new Following(m, mThree);
    followingTwo.setAccepted(true);

    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);
    applicationUserRepository.save(mFour);

    postRepository.save(p);
    postRepository.save(pTwo);
    postRepository.save(pThree);
    postRepository.save(pFour);
    postRepository.save(pFive);

    followingRepository.save(following);
    followingRepository.save(followingTwo);

    m.addWhereIsInfluencer(following);
    m.addWhereIsInfluencer(followingTwo);
    m.addPost(p);
    m.addPost(pTwo);
    p.setUser(m);
    pTwo.setUser(m);
    applicationUserRepository.save(m);
    postRepository.save(p);
    postRepository.save(pTwo);

    mThree.addWhereIsFollower(followingTwo);
    mTwo.addWhereIsFollower(following);
    mTwo.addPost(pThree);
    pThree.setUser(mTwo);
    applicationUserRepository.save(mTwo);
    postRepository.save(pThree);

    mThree.addPost(pFour);
    pFour.setUser(mThree);
    applicationUserRepository.save(mThree);
    postRepository.save(pFour);

    mFour.addPost(pFive);
    pFive.setUser(mFour);
    applicationUserRepository.save(mFour);
    postRepository.save(pFive);
  }

  @Test
  @Transactional
  void testGetFeed() {
    Pageable pageable = PageRequest.of(0, 5);

    ResponseEntity mFeed = postController.getFeed(new MockAuthentication(m), pageable);
    PageImpl mResultFeed = new PageImpl(Arrays.asList(pThree, pFour));
    assertEquals(mResultFeed.getContent().toString(), ((PageImpl) mFeed.getBody()).getContent().toString());

    ResponseEntity mTwoFeed = postController.getFeed(new MockAuthentication(mTwo), pageable);
    PageImpl mTwoResultFeed = new PageImpl(Collections.emptyList());
    assertEquals(mTwoResultFeed.getContent().toString(), ((PageImpl) mTwoFeed.getBody()).getContent().toString());
  }

  @Test
  @Transactional
  void testSearchFeed() {
    Pageable pageable = PageRequest.of(0, 5);

    ResponseEntity searchEverythingResponse = postController.searchPost(pageable, "");
    PageImpl searchEverything = new PageImpl(Arrays.asList(pFour.getId(), pFive.getId()));
    assertEquals(searchEverything.getContent().toString(), ((PageImpl<Post>) searchEverythingResponse.getBody()).map(Post::getId).getContent().toString());

    ResponseEntity searchBostonResponse = postController.searchPost(pageable, "Bost");
    PageImpl searchBoston = new PageImpl(Arrays.asList(pFour, pFive));
    assertEquals(searchBoston.getContent().toString(), ((PageImpl) searchBostonResponse.getBody()).getContent().toString());

    ResponseEntity searchLAResponse = postController.searchPost(pageable, "la");
    PageImpl searchLA = new PageImpl(Arrays.asList(pFour));
    assertEquals(searchLA.getContent().toString(), ((PageImpl) searchLAResponse.getBody()).getContent().toString());
  }

  @Test
  @Transactional
  void testGetPost() {
    ResponseEntity mOwnPostResponse = postController.getPost(new MockAuthentication(m), p.getId());
    assertEquals(p.getId(), ((Post) mOwnPostResponse.getBody()).getId());

    ResponseEntity mInfluencerPostResponse = postController.getPost(new MockAuthentication(m), pThree.getId());
    assertEquals(pThree.getId(), ((Post) mInfluencerPostResponse.getBody()).getId());

    ResponseEntity illegalResponse = postController.getPost(new MockAuthentication(mThree), p.getId());
    assertEquals(HttpStatus.FORBIDDEN, illegalResponse.getStatusCode());

    ResponseEntity noExistResponse = postController.getPost(new MockAuthentication(mTwo), (long) 1000);
    assertEquals(HttpStatus.NOT_FOUND, noExistResponse.getStatusCode());
  }

  @Test
  @Transactional
  void testNewPost() {
    Post newPost = new Post("new post", "www.com", "b", "World", "2020-10-10");

    ResponseEntity success = postController.newPost(new MockAuthentication(m), newPost);
    assertEquals(HttpStatus.OK, success.getStatusCode());

    logger.info(String.valueOf(newPost.getId()));
    assertEquals(newPost, postRepository.findById(newPost.getId()).get());

    logger.info(applicationUserRepository.findByUsername(m.getUsername()).get().getPosts().toString());
    logger.info(postRepository.findAll().toString());

    // assertTrue(applicationUserRepository.findByUsername(m.getUsername()).get().getPosts().contains(newPost));
  }

  @Test
  void testEditPost() {
    String newCity = "Somewhere";
    p.setCity(newCity);
    ResponseEntity editResponse = postController.editPost(new MockAuthentication(m), p.getId(), p);
    assertEquals(HttpStatus.OK, editResponse.getStatusCode());
    assertEquals(newCity, postRepository.findById(p.getId()).get().getCity());

    ResponseEntity notFoundResponse = postController.editPost(new MockAuthentication(m), (long) 1000, p);
    assertEquals(HttpStatus.NOT_FOUND, notFoundResponse.getStatusCode());

    ResponseEntity badResponse = postController.editPost(new MockAuthentication(mFour), p.getId(), p);
    assertEquals(HttpStatus.FORBIDDEN, badResponse.getStatusCode());
  }

  @Test
  void testDeletePost() {
    ResponseEntity deletePost = postController.deletePost(new MockAuthentication(m), p.getId());
    assertEquals(HttpStatus.OK, deletePost.getStatusCode());
    assertTrue(postRepository.findById(p.getId()).isEmpty());

    ResponseEntity notFoundResponse = postController.deletePost(new MockAuthentication(m), (long) 1000);
    assertEquals(HttpStatus.NOT_FOUND, notFoundResponse.getStatusCode());

    ResponseEntity badResponse = postController.deletePost(new MockAuthentication(mFour), pTwo.getId());
    assertEquals(HttpStatus.FORBIDDEN, badResponse.getStatusCode());
  }

}
