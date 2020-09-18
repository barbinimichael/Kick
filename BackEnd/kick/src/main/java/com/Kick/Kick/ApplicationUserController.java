package com.Kick.Kick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Kick.Kick.SecurityConstants.SIGN_UP_URL;

@RestController
public class ApplicationUserController extends Controller {

  private final ApplicationUserRepository applicationUserRepository;
  private final FollowingRepository followingRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public ApplicationUserController(ApplicationUserRepository applicationUserRepository, FollowingRepository followingRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserRepository = applicationUserRepository;
    this.followingRepository = followingRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @GetMapping("/api/applicationUsers/check")
  public ResponseEntity<String> checkAuthentication() {
    return ResponseEntity.ok("Authenticated");
  }

  @GetMapping("/api/applicationUsers/search")
  public ResponseEntity findAllBySpecification(
      @RequestParam(value = "search") String search, Pageable pageable) {
    ApplicationUserSpecificationBuilder builder = new ApplicationUserSpecificationBuilder();
    String operationSet = String.join("|", SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern =
        Pattern.compile("(\\w+?)(" + operationSet + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
    Matcher matcher = pattern.matcher(search + ",");
    while (matcher.find()) {
      builder.with(
          matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
    }
    //    SearchCriteria publicProfile = new SearchCriteria(
    //        "privateProfile",
    //        SearchOperation.getSimpleOperation(':'),
    //        false);
    //    builder.with(publicProfile);
    Specification<ApplicationUser> spec = builder.build();
    // Page<ApplicationUser> preliminary = applicationUserRepository.findAll(spec, pageable);
    Page<ApplicationUser> preliminary = applicationUserRepository.findByUsernameContainingIgnoreCase(search, pageable);
    return ResponseEntity.ok(preliminary.map(ApplicationUser::generatePubliclyVisibleUser));
  }

  @PostMapping(SIGN_UP_URL)
  public ResponseEntity signUp(@RequestBody ApplicationUser user) {
    if (applicationUserRepository.findByUsername(user.getUsername()).isPresent()) {
      return handleUsernameConflict(user.getUsername());

    } else if (applicationUserRepository.findByEmail(user.getEmail()).isPresent()) {
      return handleEmailConflict(user.getEmail());

    } else {
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      Following selfFollowing = new Following(user, user);
      selfFollowing.setAccepted(true);
      applicationUserRepository.save(user);
      followingRepository.save(selfFollowing);
      return ResponseEntity.ok("Registration successful");
    }
  }

  @GetMapping("/api/applicationUsers/self")
  public ResponseEntity getOwnApplicationUser(Authentication authentication) {
    return ResponseEntity.ok(applicationUserRepository.findByUsername(authentication.getName()));

  }

  @GetMapping("/api/applicationUsers/id/{id}")
  public ResponseEntity getApplicationUserFromId(Authentication authentication, @PathVariable @NonNull Long id) {
    Optional<ApplicationUser> maybeSearchUser = applicationUserRepository.findById(id);

    if (maybeSearchUser.isPresent()) {
      ApplicationUser searchUser = maybeSearchUser.get();
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

      if (user.equals(searchUser)) {
        return ResponseEntity.ok(user);

      } else if (checkFollowing(user, searchUser)) {
        return ResponseEntity.ok(searchUser.generateVisibleUser());

      } else {
        return ResponseEntity.ok(searchUser.generatePubliclyVisibleUser());
      }

    } else {
      return handleNotFound(id.toString());
    }
  }


  @GetMapping("/api/applicationUsers/username/{username}")
  public ResponseEntity getApplicationUserFromUsername(Authentication authentication, @PathVariable @NonNull String username) {
    Optional<ApplicationUser> maybeSearchUser = applicationUserRepository.findByUsername(username);

    if (maybeSearchUser.isPresent()) {
      ApplicationUser searchUser = maybeSearchUser.get();
      ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

      if (user.equals(searchUser)) {
        return ResponseEntity.ok(user);

      } else if (!searchUser.isPrivateProfile() || checkFollowing(user, searchUser)) {
        return ResponseEntity.ok(searchUser.generateVisibleUser());

      } else {
        return ResponseEntity.ok(searchUser.generatePubliclyVisibleUser());
      }

    } else {
      return handleNotFound(username);
    }
  }

  @GetMapping("/api/applicationUsers/followingCount/{username}")
  public ResponseEntity getApplicationUserFollowingCount(@PathVariable @NonNull String username) {
    Optional<ApplicationUser> maybeSearchUser = applicationUserRepository.findByUsername(username);

    if (maybeSearchUser.isPresent()) {
      ApplicationUser searchUser = maybeSearchUser.get();

      return ResponseEntity.ok((int) searchUser.getWhereIsInfluencer().stream().filter(Following::isAccepted).count());

    } else {
      return handleNotFound(username);
    }
  }

  @GetMapping("/api/applicationUsers/influencerCount/{username}")
  public ResponseEntity getApplicationUserInfluencerCount(@PathVariable @NonNull String username) {
    Optional<ApplicationUser> maybeSearchUser = applicationUserRepository.findByUsername(username);

    if (maybeSearchUser.isPresent()) {
      ApplicationUser searchUser = maybeSearchUser.get();

      return ResponseEntity.ok((int) searchUser.getWhereIsFollower().stream().filter(Following::isAccepted).count());

    } else {
      return handleNotFound(username);
    }
  }

  @PutMapping("/api/applicationUsers/username")
  public ResponseEntity changeApplicationUserUsername(Authentication authentication, @RequestBody String username) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

    if (username.equals(user.getUsername())) {
      return ResponseEntity.ok(user);

    } else if (applicationUserRepository.findByUsername(username).isPresent()) {
      return handleUsernameConflict(username);

    } else {
      user.setUsername(username);
      return ResponseEntity.ok(applicationUserRepository.save(user));
    }
  }

  @PutMapping("/api/applicationUsers/password")
  public ResponseEntity changeApplicationUserPassword(Authentication authentication, @RequestBody String password) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    applicationUserRepository.delete(user);
    user.setPassword(bCryptPasswordEncoder.encode(password));
    return ResponseEntity.ok(applicationUserRepository.save(user));
  }

  @PutMapping("/api/applicationUsers/email")
  public ResponseEntity changeApplicationUserEmail(Authentication authentication, @RequestBody String email) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();

    if (email.equals(user.getEmail())) {
      return ResponseEntity.ok(user);

    } else if (applicationUserRepository.findByEmail(email).isPresent()) {
      return handleEmailConflict(email);

    } else {
      user.setEmail(email);
      return ResponseEntity.ok(applicationUserRepository.save(user));
    }
  }

  @PutMapping("/api/applicationUsers/city")
  public ResponseEntity changeApplicationUserCity(Authentication authentication, @RequestBody String city) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    user.setCity(city);
    return ResponseEntity.ok(applicationUserRepository.save(user));
  }

  @PutMapping("/api/applicationUsers/country")
  public ResponseEntity changeApplicationUserCountry(Authentication authentication, @RequestBody String country) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    user.setCountry(country);
    return ResponseEntity.ok(applicationUserRepository.save(user));
  }

  @PutMapping("/api/applicationUsers/biography")
  public ResponseEntity changeApplicationUserBiography(Authentication authentication, @RequestBody String biography) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    user.setBiography(biography);
    return ResponseEntity.ok(applicationUserRepository.save(user));
  }

  @PutMapping("/api/applicationUsers/profilePictureURL")
  public ResponseEntity changeApplicationUserProfilePictureURL(Authentication authentication, @RequestBody String profilePictureURL) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    user.setProfilePictureURL(profilePictureURL);
    return ResponseEntity.ok(applicationUserRepository.save(user));
  }

  @PutMapping("/api/applicationUsers/privateProfile")
  public ResponseEntity changeApplicationUserPrivateProfile(Authentication authentication, @RequestBody String privateProfile) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    boolean value = Boolean.parseBoolean(privateProfile);
    user.setPrivateProfile(value);
    return ResponseEntity.ok(applicationUserRepository.save(user));
  }

  @DeleteMapping("/api/applicationUsers")
  public ResponseEntity deleteApplicationUser(Authentication authentication) {
    ApplicationUser user = applicationUserRepository.findByUsername(authentication.getName()).get();
    applicationUserRepository.delete(user);
    return handleSuccess("Deleted Successfully");
  }
}
