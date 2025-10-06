package com.billox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billox.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);
	
	Optional<UserEntity> findByUserId(String userId);
	
}
