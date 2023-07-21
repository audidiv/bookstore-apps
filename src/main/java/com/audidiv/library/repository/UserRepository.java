package com.audidiv.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.audidiv.library.model.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Integer>{

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
	
}
