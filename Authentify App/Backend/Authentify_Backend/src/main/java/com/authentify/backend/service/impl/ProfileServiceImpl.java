package com.authentify.backend.service.impl;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	private final EmailService eService;

	@Override
	public ProfileResponse createProfile(ProfileRequest request) {

		UserEntity newProfile = convertToUserEntity(request);

		if (!uRepo.existsByEmail(request.getEmail())) {
			newProfile = uRepo.save(newProfile);
			return convertToProfileResponse(newProfile);
		} else {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}

	}

	@Override
	public ProfileResponse getProfile(String email) {

		UserEntity existingUser = uRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

		return convertToProfileResponse(existingUser);

	}

	@Override
	public void sendResetOtp(String email) {

		UserEntity existingEntity = uRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

		// Generate 6 digit otp
		String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

		// Calculate Expiry time (current time + 15 mins in milliseconds)
		long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000);

		// update user entity
		existingEntity.setResetOtp(otp);
		existingEntity.setResetOtpExpireAt(expiryTime);

		// save to DB
		uRepo.save(existingEntity);

		try {

			// send the reset otp email
			eService.sendResetOtpEmail(existingEntity.getEmail(), otp);

		} catch (Exception ex) {
			throw new RuntimeException("Unable to send email");
		}

	}

	@Override
	public void resetPassword(String email, String otp, String newPassword) {

		UserEntity existingUser = uRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

		if (existingUser.getResetOtp() == null || !existingUser.getResetOtp().equals(otp)) {
			throw new RuntimeException("Invalid OTP");
		}

		if (existingUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
			throw new RuntimeException("OTP Expired");
		}

		existingUser.setPassword(passwordEncoder.encode(newPassword));
		existingUser.setResetOtp(null);
		existingUser.setResetOtpExpireAt(0L);

		uRepo.save(existingUser);

	}

	@Override
	public void sendOtp(String email) {

		UserEntity existingUser = uRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

		if (existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()) {
			return;
		}

		// Else generate 6 digit otp
		// Generate 6 digit otp
		String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

		// Calculate Expiry time (current time + 24 hrs in milliseconds)
		long expiryTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
		
		// Update the userEntity
		existingUser.setVerifyOtp(otp);
		existingUser.setVerifyOtpExpireAt(expiryTime);
		
		// save in DB
		uRepo.save(existingUser);

	}

	@Override
	public void verifyOtp(String email, String otp) {

	}

	@Override
	public String getLoggedInUserId(String email) {

		UserEntity existingUser = uRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

		return existingUser.getUserId();

	}

	private UserEntity convertToUserEntity(ProfileRequest request) {

		return UserEntity.builder().email(request.getEmail()).userId(UUID.randomUUID().toString())
				.name(request.getName()).password(passwordEncoder.encode(request.getPassword()))
				.isAccountVerified(false).resetOtpExpireAt(0L).verifyOtp(null).verifyOtpExpireAt(0l).resetOtp(null)
				.build();

	}

	private ProfileResponse convertToProfileResponse(UserEntity newProfile) {

		return ProfileResponse.builder().email(newProfile.getEmail()).userId(newProfile.getUserId())
				.name(newProfile.getName()).isAccountVerified(newProfile.getIsAccountVerified()).build();

	}

}
