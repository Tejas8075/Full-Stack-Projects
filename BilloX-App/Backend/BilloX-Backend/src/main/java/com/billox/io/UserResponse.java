package com.billox.io;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private String userId;
	
	private String name;

	private String email;

	private String password;
	
	private Timestamp createdAt;

	private Timestamp updatedAt;
	
	private String role;
}
