package com.eventorganizer.event_org.repository;

import com.eventorganizer.event_org.model.App_User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<App_User, Long> {
    boolean existsByUsername(String username);
    Optional<App_User> findByUsername(String username);

}
