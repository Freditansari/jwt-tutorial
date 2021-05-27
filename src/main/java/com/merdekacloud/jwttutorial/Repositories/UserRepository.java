package com.merdekacloud.jwttutorial.Repositories;

import com.merdekacloud.jwttutorial.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername (String username);

    Boolean existsByUsername(String username);
    Boolean existsbyEmail(String email);
}
