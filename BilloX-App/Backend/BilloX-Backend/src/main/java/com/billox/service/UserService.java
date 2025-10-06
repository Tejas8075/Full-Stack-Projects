package com.billox.service;

import java.util.List;

import com.billox.io.UserRequest;
import com.billox.io.UserResponse;

public interface UserService {

	UserResponse createUser(UserRequest request);
	
	String getUserRole(String email);
	
	List<UserResponse> readUsers();
	
	void deleteUser(String id);
	
}
