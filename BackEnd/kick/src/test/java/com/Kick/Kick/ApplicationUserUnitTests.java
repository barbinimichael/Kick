package com.Kick.Kick;

import com.Kick.Kick.ApiError;
import com.Kick.Kick.ApplicationUser;
import com.Kick.Kick.ApplicationUserController;
import com.Kick.Kick.ApplicationUserRepository;
import com.Kick.Kick.PostController;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Kick.Kick.MockAuthentication;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationUserUnitTests {

  private final ApplicationUserController applicationUserController;
  private final ApplicationUserRepository applicationUserRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  private ApplicationUser m;

  private ApplicationUser mTwo;

  private ApplicationUser mThree;

  @Autowired
  ApplicationUserUnitTests(ApplicationUserController applicationUserController,
                           ApplicationUserRepository applicationUserRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.applicationUserController = applicationUserController;
    this.applicationUserRepository = applicationUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @BeforeEach
  void intiEntities() {
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

    this.applicationUserRepository.deleteAll();
  }

  @Test
  void signUpTest() {
    ResponseEntity successfulSignUp = applicationUserController.signUp(m);
    assertThat(successfulSignUp.getStatusCode().equals(HttpStatus.OK));
    assertEquals(m, applicationUserRepository.findByUsername(m.getUsername()).get());

    ResponseEntity successfulSignUpTwo = applicationUserController.signUp(mTwo);
    assertThat(successfulSignUpTwo.getStatusCode().equals(HttpStatus.OK));
    assertEquals(mTwo , applicationUserRepository.findByUsername(mTwo.getUsername()).get());

    ResponseEntity badSignUpDuplicate = applicationUserController.signUp(m);
    assertThat(badSignUpDuplicate.getStatusCode().equals(HttpStatus.CONFLICT));

    mThree.setUsername(m.getUsername());
    ResponseEntity badSignUpUsername = applicationUserController.signUp(mThree);
    assertThat(badSignUpUsername.getStatusCode().equals(HttpStatus.CONFLICT));

    mThree.setUsername(m.getEmail());
    ResponseEntity badSignUpEmail = applicationUserController.signUp(mThree);
    assertThat(badSignUpEmail.getStatusCode().equals(HttpStatus.CONFLICT));
  }

  @Test
  void searchTest() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    Pageable pageable = PageRequest.of(0, 5);

    PageImpl resultExact = new PageImpl(Arrays.asList(mThree.generatePublicUser()));
    ResponseEntity responseExact =
        applicationUserController.findAllBySpecification("search=username:*a*", pageable);

    assertEquals(resultExact.getContent(), ((PageImpl) responseExact.getBody()).getContent());

    PageImpl noResult = new PageImpl(Collections.emptyList());
    ResponseEntity noResponse =
        applicationUserController.findAllBySpecification("search=username:*z*", pageable);

    assertEquals(noResult.getContent(), ((PageImpl) noResponse.getBody()).getContent());
  }

  @Test
  void userSelfTest() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    ResponseEntity responseOne =
        applicationUserController.getOwnApplicationUser(new MockAuthentication(m));
    ResponseEntity responseTwo =
        applicationUserController.getOwnApplicationUser(new MockAuthentication(mTwo));
    ResponseEntity responseThree =
        applicationUserController.getOwnApplicationUser(new MockAuthentication(mThree));

    ApplicationUser userOne = ((Optional<ApplicationUser>) responseOne.getBody()).get();
    ApplicationUser userTwo = ((Optional<ApplicationUser>) responseTwo.getBody()).get();
    ApplicationUser userThree = ((Optional<ApplicationUser>) responseThree.getBody()).get();

    assertEquals(m, userOne);
    assertEquals(mTwo, userTwo);
    assertEquals(mThree, userThree);
  }

  @Test
  @Transactional
  void getUserFromIDTest() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    ResponseEntity responseSame =
        applicationUserController.getApplicationUserFromId(new MockAuthentication(m), m.getId());
    ResponseEntity responsePrivateOne =
        applicationUserController.getApplicationUserFromId(new MockAuthentication(mTwo), m.getId());
    ResponseEntity responsePrivateTwo =
        applicationUserController.getApplicationUserFromId(new MockAuthentication(mThree), m.getId());

    assertEquals(m, responseSame.getBody());
    assertEquals(m.generatePrivateUser(), responsePrivateOne.getBody());
    assertEquals(m.generatePrivateUser(), responsePrivateTwo.getBody());

    ResponseEntity notFound =
        applicationUserController.getApplicationUserFromId(new MockAuthentication(m), (long) 1000);
    assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
    assertEquals("1000", ((ApiError) notFound.getBody()).getErrorValue());
  }

  @Test
  @Transactional
  void getUserFromUsername() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    ResponseEntity responseSame =
        applicationUserController.getApplicationUserFromUsername(new MockAuthentication(m), m.getUsername());
    ResponseEntity responsePrivateOne =
        applicationUserController.getApplicationUserFromUsername(new MockAuthentication(mTwo), m.getUsername());
    ResponseEntity responsePrivateTwo =
        applicationUserController.getApplicationUserFromUsername(new MockAuthentication(mThree), m.getUsername());

    assertEquals(m, responseSame.getBody());
    assertEquals(m.generatePrivateUser(), responsePrivateOne.getBody());
    assertEquals(m.generatePrivateUser(), responsePrivateTwo.getBody());

    ResponseEntity notFound =
        applicationUserController.getApplicationUserFromUsername(new MockAuthentication(m), "not found");
    assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
    assertEquals("not found", ((ApiError) notFound.getBody()).getErrorValue());
  }

  @Test
  void changeUsernameTest() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    String oldUsername = m.getUsername();
    String newUsername = "new username";

    ResponseEntity sameUsername = applicationUserController.changeApplicationUserUsername(new MockAuthentication(m), m.getUsername());
    assertEquals("Saved username", sameUsername.getBody());
    assertEquals(HttpStatus.OK, sameUsername.getStatusCode());
    assertEquals(oldUsername, applicationUserRepository.findByUsername(m.getUsername()).get().getUsername());

    ResponseEntity takenUsername = applicationUserController.changeApplicationUserUsername(new MockAuthentication(m), mTwo.getUsername());
    assertEquals("Username was already taken", ((ApiError) takenUsername.getBody()).getError());
    assertEquals(HttpStatus.CONFLICT, takenUsername.getStatusCode());
    assertEquals(oldUsername, applicationUserRepository.findByUsername(m.getUsername()).get().getUsername());

    ResponseEntity successUsername = applicationUserController.changeApplicationUserUsername(new MockAuthentication(m), newUsername);
    assertEquals("Saved username", successUsername.getBody());
    assertEquals(HttpStatus.OK, successUsername.getStatusCode());
    assertEquals(newUsername, applicationUserRepository.findByUsername(newUsername).get().getUsername());

    ResponseEntity lastUsername = applicationUserController.changeApplicationUserUsername(new MockAuthentication(mTwo), oldUsername);
    assertEquals("Saved username", lastUsername.getBody());
    assertEquals(HttpStatus.OK, lastUsername.getStatusCode());
    assertEquals(newUsername, applicationUserRepository.findByUsername(newUsername).get().getUsername());
    assertEquals(oldUsername, applicationUserRepository.findByUsername(oldUsername).get().getUsername());
  }

  @Test
  void changePasswordTest() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    String newPassword = "another great password";

    ResponseEntity passwordResponse = applicationUserController.changeApplicationUserPassword(new MockAuthentication(m), newPassword);

    assertEquals(HttpStatus.OK, passwordResponse.getStatusCode());
    assertEquals("Saved password", passwordResponse.getBody());
    assertThat(bCryptPasswordEncoder.matches(newPassword, applicationUserRepository.findByUsername(m.getUsername()).get().getPassword()));
  }

  @Test
  void changeEmailTest() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    String oldEmail = m.getEmail();
    String newEmail = "new email";

    ResponseEntity sameEmail = applicationUserController.changeApplicationUserEmail(new MockAuthentication(m), m.getEmail());
    assertEquals("Saved email", sameEmail.getBody());
    assertEquals(HttpStatus.OK, sameEmail.getStatusCode());
    assertEquals(oldEmail, applicationUserRepository.findByUsername(m.getUsername()).get().getEmail());

    ResponseEntity takenEmail = applicationUserController.changeApplicationUserEmail(new MockAuthentication(m), mTwo.getEmail());
    assertEquals("Email was already taken", ((ApiError) takenEmail.getBody()).getError());
    assertEquals(HttpStatus.CONFLICT, takenEmail.getStatusCode());
    assertEquals(oldEmail, applicationUserRepository.findByUsername(m.getUsername()).get().getEmail());

    ResponseEntity successEmail = applicationUserController.changeApplicationUserEmail(new MockAuthentication(m), newEmail);
    assertEquals("Saved email", successEmail.getBody());
    assertEquals(HttpStatus.OK, successEmail.getStatusCode());
    assertEquals(newEmail, applicationUserRepository.findByUsername(m.getUsername()).get().getEmail());

    ResponseEntity lastEmail = applicationUserController.changeApplicationUserEmail(new MockAuthentication(mTwo), oldEmail);
    assertEquals("Saved email", lastEmail.getBody());
    assertEquals(HttpStatus.OK, lastEmail.getStatusCode());
    assertEquals(newEmail, applicationUserRepository.findByUsername(m.getUsername()).get().getEmail());
    assertEquals(oldEmail, applicationUserRepository.findByUsername(mTwo.getUsername()).get().getEmail());
  }

  @Test
  void changeCityTest() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    String newCity = "cool city";

    ResponseEntity cityResponse = applicationUserController.changeApplicationUserCity(new MockAuthentication(m), newCity);

    assertEquals(HttpStatus.OK, cityResponse.getStatusCode());
    assertEquals("Saved city", cityResponse.getBody());
    assertEquals(newCity, applicationUserRepository.findByUsername(m.getUsername()).get().getCity());
  }

  @Test
  void changeCountry() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    String newCountry = "cool country";

    ResponseEntity countryResponse = applicationUserController.changeApplicationUserCountry(new MockAuthentication(m), newCountry);

    assertEquals(HttpStatus.OK, countryResponse.getStatusCode());
    assertEquals("Saved country", countryResponse.getBody());
    assertEquals(newCountry, applicationUserRepository.findByUsername(m.getUsername()).get().getCountry());
  }

  @Test
  void changeBiography() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    String newBiography = "new biography";

    ResponseEntity biographyResponse = applicationUserController.changeApplicationUserBiography(new MockAuthentication(m), newBiography);

    assertEquals(HttpStatus.OK, biographyResponse.getStatusCode());
    assertEquals("Saved biography", biographyResponse.getBody());
    assertEquals(newBiography, applicationUserRepository.findByUsername(m.getUsername()).get().getBiography());
  }

  @Test
  void changeProfilePictureURL() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    String newProfilePictureURL = "new profilePictureURL";

    ResponseEntity profilePictureURLResponse = applicationUserController.changeApplicationUserProfilePictureURL(new MockAuthentication(m), newProfilePictureURL);

    assertEquals(HttpStatus.OK, profilePictureURLResponse.getStatusCode());
    assertEquals("Saved profilePictureURL", profilePictureURLResponse.getBody());
    assertEquals(newProfilePictureURL, applicationUserRepository.findByUsername(m.getUsername()).get().getProfilePictureURL());
  }

  @Test
  void changePrivateProfile() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    ResponseEntity response = applicationUserController.changeApplicationUserPrivateProfile(new MockAuthentication(m), "false");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Saved privateProfile", response.getBody());
    assertEquals(false, applicationUserRepository.findByUsername(m.getUsername()).get().isPrivateProfile());
  }

  @Test
  void deleteUser() {
    applicationUserRepository.save(m);
    applicationUserRepository.save(mTwo);
    applicationUserRepository.save(mThree);

    assertEquals(m, applicationUserRepository.findByUsername(m.getUsername()).get());

    ResponseEntity response = applicationUserController.deleteApplicationUser(new MockAuthentication(m));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Deleted Successfully", response.getBody());
    assertTrue(applicationUserRepository.findByUsername(m.getUsername()).isEmpty());
  }

}

