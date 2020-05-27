package com.Kick.Kick;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KickApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private final ApplicationUserRepository applicationUserRepository;
	private final PostRepository postRepository;

	private final FollowingRepository followingRepository;

	private final LikePostRepository likePostRepository;
	private final CommentPostRepository commentPostRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(PostController.class);

	private final ApplicationUser m = new ApplicationUser("miker",
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

	private final ApplicationUser mTwo = new ApplicationUser("mikey",
			"secure password",
			"Mikey",
			"Boz",
			"ye@me.you",
			"2020-10-11",
			"somewhere",
			"eartch",
			"MALE",
			"Heyo", "image.im",
			true);

	private final Post p = new Post("Hi",
			"www.link.le",
			"Boston",
			"USA",
			Instant.now(),
			new HashSet<>(),
			new HashSet<>());

	private final Post pTwo = new Post("Hi again",
			"www.link2.le",
			"Boston",
			"USA",
			Instant.now(),
			new HashSet<>(),
			new HashSet<>());

	private final Following following = new Following(m, mTwo);

	private final LikePost likeOne = new LikePost(mTwo, p);

	@Autowired
	KickApplicationTests(ApplicationUserRepository applicationUserRepository,
																PostRepository postRepository,
																FollowingRepository followingRepository,
																LikePostRepository likePostRepository,
																CommentPostRepository commentPostRepository,
																BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.applicationUserRepository = applicationUserRepository;
		this.postRepository = postRepository;
		this.followingRepository = followingRepository;
		this.likePostRepository = likePostRepository;
		this.commentPostRepository = commentPostRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Test
	void registrationTest() throws Exception {
		this.applicationUserRepository.deleteAll();
		logger.info(objectMapper.writeValueAsString(m));

		MvcResult result = mockMvc.perform(post("http://localhost:8081/api/applicationUsers/sign-up")
				.contentType(MediaType.APPLICATION_JSON)
				.accept("*/*")
				.content(applicationUserToJSON(m)))
				.andExpect(status().isOk())
				.andReturn();

		ApplicationUser userEntity = applicationUserRepository.findByUsername("miker").get();

		assertThat(userEntity.getUsername()).isEqualTo("miker");
		assertTrue(bCryptPasswordEncoder.matches("password", userEntity.getPassword()));
		assertThat(userEntity.getFirstName()).isEqualTo("Mike");
		assertThat(userEntity.getLastName()).isEqualTo("B");
		assertThat(userEntity.getEmail()).isEqualTo("mbarbzzz@jmail.com");
		assertThat(userEntity.getBirthday().toString()).isEqualTo("2020-05-10T00:00:00Z");
		assertThat(userEntity.getCity()).isEqualTo("Boston");
		assertThat(userEntity.getCountry()).isEqualTo("USA");
		assertThat(userEntity.getGender()).isEqualTo(Gender.MALE);
		assertThat(userEntity.getBiography()).isEqualTo("Hi");
		assertThat(userEntity.getProfilePictureURL()).isEqualTo("https://imgur.com/a/qKEjLCD");
		assertThat(userEntity.isPrivateProfile()).isEqualTo(true);
	}

	@Test
	void signInTest() throws Exception {
		this.applicationUserRepository.deleteAll();
		m.setPassword(bCryptPasswordEncoder.encode(m.getPassword()));
		this.applicationUserRepository.save(m);

		String login = "{" +
				"\"username\":\"miker\"," +
				"\"password\":\"password\"" +
				"}";

		MvcResult result = mockMvc.perform(post("http://localhost:8081/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept("*/*")
				.content(login))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		logger.info(content);
		String header = result.getResponse().getHeader("Authorization");
		logger.info(header);

	}

	void getUserDataTest() throws Exception {
		this.applicationUserRepository.deleteAll();
		m.setPassword(bCryptPasswordEncoder.encode(m.getPassword()));
		this.applicationUserRepository.save(m);

		String login = "{" +
				"\"username\":\"miker\"," +
				"\"password\":\"password\"" +
				"}";

		logger.info("USER DATA TEST LOGGING IN");
		MvcResult result = mockMvc.perform(post("http://localhost:8081/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept("*/*")
				.content(login))
				.andExpect(status().isOk())
				.andReturn();

		String authorizationHeader = result.getResponse().getHeader("Authorization");

		logger.info("USER DATA TEST GETTING OWN DATA");
		MvcResult resultTwo = mockMvc.perform(get("http://localhost:8081/api/applicationUsers/self")
				.contentType(MediaType.APPLICATION_JSON)
				.accept("*/*")
				.header("Authorization", authorizationHeader))
				.andExpect(status().isOk())
				.andReturn();

		String response = resultTwo.getResponse().getContentAsString();
		ApplicationUser userEntity = objectMapper.readValue(response, ApplicationUser.class);

		assertThat(userEntity.getUsername()).isEqualTo("miker");
		// expect no password in body
		assertFalse(bCryptPasswordEncoder.matches("password", userEntity.getPassword()));
		assertNull(userEntity.getPassword());
		assertThat(userEntity.getFirstName()).isEqualTo("Mike");
		assertThat(userEntity.getLastName()).isEqualTo("B");
		assertThat(userEntity.getEmail()).isEqualTo("mbarbzzz@jmail.com");
		assertThat(userEntity.getBirthday().toString()).isEqualTo("2020-05-10T00:00:00Z");
		assertThat(userEntity.getCity()).isEqualTo("Boston");
		assertThat(userEntity.getCountry()).isEqualTo("USA");
		assertThat(userEntity.getGender()).isEqualTo(Gender.MALE);
		assertThat(userEntity.getBiography()).isEqualTo("Hi");
		assertThat(userEntity.getProfilePictureURL()).isEqualTo("https://imgur.com/a/qKEjLCD");
		assertThat(userEntity.isPrivateProfile()).isEqualTo(true);
	}

	@Test
	void createPostTest() throws Exception {

	}

	@Test
	void FollowTest() throws Exception {

	}

	@Test
	void LikeTest() throws Exception {

	}

	@Test
	void CommentTest() throws Exception {

	}

	@Test
	void SecurityTest() throws Exception {

	}

	public String applicationUserToJSON(ApplicationUser applicationUser) {
		// Since password is not serialized, need another means
		String[] raw = {
				"username", applicationUser.getUsername(),
				"password", applicationUser.getPassword(),
				"firstName", applicationUser.getFirstName(),
				"lastName", applicationUser.getLastName(),
				"email", applicationUser.getEmail(),
				"birthday", String.valueOf(applicationUser.getBirthday()),
				"city", applicationUser.getCity(),
				"country", applicationUser.getCountry(),
				"gender", String.valueOf(applicationUser.getGender()),
				"biography", applicationUser.getBiography(),
				"profilePictureURL", applicationUser.getProfilePictureURL(),
				"privateProfile", String.valueOf(applicationUser.isPrivateProfile())
		};
		StringBuilder newString = new StringBuilder();
		newString.append("{");
		for (int i = 0; i < raw.length; i++) {
			newString.append("\"").append(raw[i]).append("\"");
			if (i % 2 == 0) {
				newString.append(":");
			} else {
				newString.append(",");
			}
		}
		newString.deleteCharAt(newString.length() - 1);
		newString.append("}");

		return newString.toString();
	}

}
