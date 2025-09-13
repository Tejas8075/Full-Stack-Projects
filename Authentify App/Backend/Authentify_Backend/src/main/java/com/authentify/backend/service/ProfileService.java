package com.authentify.backend.service;

import com.authentify.backend.dto.ProfileRequest;
import com.authentify.backend.dto.ProfileResponse;

public interface ProfileService {

	ProfileResponse createProfile(ProfileRequest request);
	
	ProfileResponse getProfile(String email);
	
	void sendResetOtp(String email);
	
	void resetPassword(String email, String otp, String newPassword);
	
	void sendOtp(String email);
	
	void verifyOtp(String email, String otp);
	
	String getLoggedInUserId(String email);
	
}
