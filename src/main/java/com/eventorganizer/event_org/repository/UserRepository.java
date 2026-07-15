package com.eventorganizer.event_org.repository;

import com.eventorganizer.event_org.model.App_User;
import com.eventorganizer.event_org.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<App_User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<App_User> findByUsername(String username);
    Optional<App_User> findByEmail(String email);
    @Query("SELECT u.email FROM App_User u")
    List<String> findAllEmails();

    @Query("SELECT u FROM App_User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")

    List<App_User> searchUsersByAdmin(String keyword);


}
