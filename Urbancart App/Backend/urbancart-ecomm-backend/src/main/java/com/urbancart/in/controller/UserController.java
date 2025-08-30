package com.urbancart.in.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbancart.in.exception.UserException;
import com.urbancart.in.model.User;
import com.urbancart.in.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

		System.out.println("/api/users/profile");
		User user = userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

}
