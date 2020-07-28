package com.Kick.Kick;

public class SecurityConstants {

  // local testing
  public static final String SECRET = "SecretKeyToGenJWTs";
  public static final long EXPIRATION_TIME = 3_600_000; // 1 hour

  // public static final String SECRET = System.getenv("SECRET");
  // public static final long EXPIRATION_TIME = 86_400_000;
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String SIGN_UP_URL = "/api/applicationUsers/sign-up";
  public static final String SIGN_IN_URL = "/login";
}
