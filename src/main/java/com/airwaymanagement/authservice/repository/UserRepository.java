package com.airwaymanagement.authservice.repository;

import com.airwaymanagement.authservice.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long userId);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

}
