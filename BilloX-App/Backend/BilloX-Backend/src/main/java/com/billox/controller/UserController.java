package com.billox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.billox.io.UserRequest;
import com.billox.io.UserResponse;
import com.billox.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

	private final UserService uService;
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	UserResponse registerUser(@RequestBody UserRequest request) {
		
		try {
			return uService.createUser(request);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to create user" + e.getMessage());
		}
		
	}
	
	@GetMapping("/users")
	List<UserResponse> readUsers(){
		
		return uService.readUsers();
	}
	
	@DeleteMapping("/users/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteUsers(@PathVariable String id) {
		
		try {
			uService.deleteUser(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		
	}
	
}
