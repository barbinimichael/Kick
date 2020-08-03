package com.Kick.Kick;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Page<Post> findByUserInOrderByTimeDesc(List<ApplicationUser> userList, Pageable pageable);
    Page<Post> findByUserOrderByTimeAsc(ApplicationUser user, Pageable pageable);
    Page<Post> findByUserPrivateProfileAndCaptionContaining(boolean privateProfile, String caption, Pageable pageable);
    Page<Post> findByUserPrivateProfileAndCityLike(boolean privateProfile, String city, Pageable pageable);
    Page<Post> findByUserPrivateProfileAndCountryLike(boolean privateProfile, String country, Pageable pageable);
    Page<Post> findAllByUserPrivateProfile(boolean privateProfile, Pageable pageable);
    @Query(value = "SELECT p FROM Post p WHERE p.user.privateProfile = false AND lower(p.caption) LIKE lower(concat('%', :search,'%')) " +
        "OR p.user.privateProfile = false AND lower(p.city) LIKE lower(concat('%', :search,'%')) " +
        "OR p.user.privateProfile = false AND lower(p.country) LIKE lower(concat('%', :search,'%'))")
    Page<Post> findByAttributes(@Param("search") String search, Pageable pageable);
}
