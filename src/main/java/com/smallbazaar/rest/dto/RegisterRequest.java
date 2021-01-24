package com.smallbazaar.rest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterRequest {
	@NotBlank(message = "Email is required")
	@Email
	@NotNull
	private String email;

	@Size(max = 20)
	@NotNull
	@NotBlank(message = "firstname is required")
	private String firstName;
	
	@Size(max = 20)
	@NotNull
	@NotBlank(message = "lastname is required")
	private String lastName;
	
	@Size(max = 20)
	@NotNull
	@NotBlank(message = "username is required")
	private String username;

	@Size(min=8,max = 20,message = "Password must be between 8-20 character")
	@NotNull
	@NotBlank(message = "password is required")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}