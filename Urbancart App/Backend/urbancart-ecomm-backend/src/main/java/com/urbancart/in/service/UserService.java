package com.urbancart.in.service;

//import java.util.List;

import com.urbancart.in.exception.UserException;
import com.urbancart.in.model.User;


public interface UserService {

	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public User findUserByEmail(String email) throws UserException;


}
