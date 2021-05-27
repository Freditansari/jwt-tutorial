package com.merdekacloud.jwttutorial.Repositories;

import com.merdekacloud.jwttutorial.Models.Role;
import com.merdekacloud.jwttutorial.Models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName rolename);
}
