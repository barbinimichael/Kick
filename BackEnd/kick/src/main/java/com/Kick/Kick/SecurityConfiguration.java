package com.Kick.Kick;

import static com.Kick.Kick.SecurityConstants.SIGN_IN_URL;
import static com.Kick.Kick.SecurityConstants.SIGN_UP_URL;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final ApplicationUserDetailsServiceImpl userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ApplicationUserRepository applicationUserRepository;

  @Autowired
  public SecurityConfiguration(ApplicationUserDetailsServiceImpl userDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationUserRepository applicationUserRepository) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.applicationUserRepository = applicationUserRepository;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
//        .requiresChannel()
//        .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
//        .requiresSecure().and()
        .cors().and().csrf().disable().authorizeRequests()
        // .anyRequest().permitAll()
        .antMatchers("/main.css", "/", "/index.html", "/built/bundle.js", SIGN_UP_URL, SIGN_IN_URL).permitAll()
        .anyRequest().authenticated().and()
        .addFilter(new JWTAuthenticationFilter(authenticationManager(), this.applicationUserRepository))
        .addFilter(new JWTAuthorizationFilter(authenticationManager()))
        // this disables session creation on Spring Security
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
    config.setAllowedMethods(Arrays.asList("GET", "PUT", "DELETE", "POST"));
    config.setExposedHeaders(List.of("Authorization"));
    config.setAllowedOrigins(List.of("https://kick-share.com"));
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
