package com.Kick.Kick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Set;

public class Controller {

  private static final Logger logger = LoggerFactory.getLogger(Controller.class);

  protected ResponseEntity<ApiError> handleBadCredentials(String errorValue) {
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(new ApiError("Bad Credentials", errorValue));
  }

  protected ResponseEntity<ApiError> handleUsernameConflict(String errorValue) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ApiError("Username was already taken", errorValue));
  }

  protected ResponseEntity<ApiError> handleEmailConflict(String errorValue) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ApiError("Email was already taken", errorValue));
  }

  protected ResponseEntity<String> handleSuccess(String message) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(message);
  }

  protected ResponseEntity<ApiError> handleNotFound(String errorValue) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ApiError("Request parameter not found", errorValue));
  }

  protected boolean checkFollowing(ApplicationUser follower, ApplicationUser influencer) {
    Set<Following> influencers = follower.getWhereIsInfluencer();

    for (Following f : influencers) {
      if (f.getInfluencer() == null) {
        continue;
      } else if (f.getInfluencer().equals(influencer)) {
        return f.isAccepted();
      }
    }
    return false;
  }
}

class ApiError {
  private String error;
  private String errorValue;

  ApiError(String error, String errorValue) {
    this.error = error;
    this.errorValue = errorValue;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getErrorValue() {
    return errorValue;
  }

  public void setErrorValue(String errorValue) {
    this.errorValue = errorValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ApiError apiError = (ApiError) o;
    return Objects.equals(error, apiError.error) &&
        Objects.equals(errorValue, apiError.errorValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error, errorValue);
  }

  @Override
  public String toString() {
    return "ApiError{" +
        "error='" + error + '\'' +
        ", errorValue='" + errorValue + '\'' +
        '}';
  }
}
