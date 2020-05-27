package com.Kick.Kick;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class ApplicationUserDetailsServiceImpl implements UserDetailsService {
  private ApplicationUserRepository applicationUserRepository;

  public ApplicationUserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
    this.applicationUserRepository = applicationUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<ApplicationUser> maybeApplicationUser = applicationUserRepository.findByUsername(username);
    if (maybeApplicationUser.isEmpty()) {
      throw new UsernameNotFoundException(username);
    }
    ApplicationUser applicationUser = maybeApplicationUser.get();
    return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
  }
}