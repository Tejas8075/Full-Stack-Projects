package com.billox.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.billox.entity.UserEntity;
import com.billox.io.UserRequest;
import com.billox.io.UserResponse;
import com.billox.repository.UserRepository;
import com.billox.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository uRepo;

	private final PasswordEncoder pEncoder;

	@Override
	public UserResponse createUser(UserRequest request) {

		UserEntity newUser = convertToEntity(request);

		newUser = uRepo.save(newUser);

		return convertToResponse(newUser);

	}

	@Override
	public String getUserRole(String email) {

		UserEntity existingUser = uRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		return existingUser.getRole();

	}

	@Override
	public List<UserResponse> readUsers() {

		return uRepo.findAll().stream().map(user -> convertToResponse(user)).collect(Collectors.toList());

	}

	@Override
	public void deleteUser(String id) {

		UserEntity existingUser = uRepo.findByUserId(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		uRepo.delete(existingUser);

	}

	private UserResponse convertToResponse(UserEntity newUser) {
		return UserResponse.builder().name(newUser.getName()).email(newUser.getEmail()).userId(newUser.getUserId())
				.createdAt(newUser.getCreatedAt()).updatedAt(newUser.getUpdatedAt()).role(newUser.getRole()).build();
	}

	private UserEntity convertToEntity(UserRequest request) {
		return UserEntity.builder().userId(UUID.randomUUID().toString()).email(request.getEmail())
				.password(pEncoder.encode(request.getPassword())).role(request.getRole().toUpperCase())
				.name(request.getName()).build();
	}

}
