package com.Kick.Kick;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {
  Optional<ApplicationUser> findByUsername(String username);
  Optional<ApplicationUser> findByEmail(String email);
}
