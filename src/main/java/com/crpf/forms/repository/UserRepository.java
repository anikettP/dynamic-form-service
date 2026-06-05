package com.crpf.forms.repository;

import com.crpf.forms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndEnabledTrue(String username);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
