package com.authentify.backend.service;

import com.authentify.backend.dto.ProfileRequest;
import com.authentify.backend.dto.ProfileResponse;

public interface ProfileService {

	ProfileResponse createProfile(ProfileRequest request);
	
	
	
}
