package com.secutiry.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.secutiry.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findByName(String name);
}
