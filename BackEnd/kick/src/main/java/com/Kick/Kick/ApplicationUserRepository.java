package com.Kick.Kick;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {
  Optional<ApplicationUser> findByUsername(String username);
  Optional<ApplicationUser> findByEmail(String email);
  Page<ApplicationUser> findByUsernameContainingIgnoreCase(String search, Pageable pageable);
}
