package com.authentify.backend.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.authentify.backend.dto.ProfileRequest;
import com.authentify.backend.dto.ProfileResponse;
import com.authentify.backend.entity.UserEntity;
import com.authentify.backend.repository.UserRepository;
import com.authentify.backend.service.ProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final UserRepository uRepo;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public ProfileResponse createProfile(ProfileRequest request) {

		UserEntity newProfile = convertToUserEntity(request);

		if(!uRepo.existsByEmail(request.getEmail())) {
			newProfile = uRepo.save(newProfile);
			return convertToProfileResponse(newProfile);
		}
		else {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}

	}

	private UserEntity convertToUserEntity(ProfileRequest request) {

		return UserEntity.builder()
				         .email(request.getEmail())
				         .userId(UUID.randomUUID().toString())
				         .name(request.getName())
				         .password(passwordEncoder.encode(request.getPassword()))
				         .isAccountVerified(false)
				         .resetOtpExpireAt(0L)
				         .verifyOtp(null)
				         .verifyOtpExpireAt(0l)
				         .resetOtp(null)
				         .build();

	}

	private ProfileResponse convertToProfileResponse(UserEntity newProfile) {

		return ProfileResponse.builder().email(newProfile.getEmail()).userId(newProfile.getUserId())
				.name(newProfile.getName()).isAccountVerified(newProfile.getIsAccountVerified()).build();

	}

}
