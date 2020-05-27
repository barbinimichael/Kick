package com.Kick.Kick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;

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
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public DatabaseLoader(ApplicationUserRepository applicationUserRepository,
                        PostRepository postRepository,
                        FollowingRepository followingRepository,
                        LikePostRepository likePostRepository, CommentPostRepository commentPostRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.postRepository = postRepository;
    this.followingRepository = followingRepository;
    this.likePostRepository = likePostRepository;
    this.commentPostRepository = commentPostRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public void run(String... strings) throws Exception {

    ApplicationUser m = new ApplicationUser("mbarbzzz",
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
        new HashSet<>());

    Post pTwo = new Post("Hi again",
        "www.link2.le",
        "Boston",
        "USA",
        Instant.now(),
        new HashSet<>(),
        new HashSet<>());

    Following following = new Following(mTwo, m);

    LikePost likeOne = new LikePost(mTwo, p);

    mTwo.addLike(likeOne);

    m.addFollower(following);
    mTwo.addInfluencer(following);

    p.setUser(m);
    pTwo.setUser(m);

    m.addPost(p);
    m.addPost(pTwo);

    m.setPassword(bCryptPasswordEncoder.encode(m.getPassword()));
    this.applicationUserRepository.save(m);

    mTwo.setPassword(bCryptPasswordEncoder.encode(mTwo.getPassword()));
    this.applicationUserRepository.save(mTwo);

    this.postRepository.save(p);
    this.postRepository.save(pTwo);

    this.followingRepository.save(following);

    this.likePostRepository.save(likeOne);

  }
}
