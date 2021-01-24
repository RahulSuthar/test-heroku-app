package com.smallbazaar.rest.dto;

import java.time.Instant;

public class AuthenticationResponse {

	private String token;
	private String userId;
	private String role;
	private String refreshToken;
	private Instant expiresAt;
	
	public AuthenticationResponse(String token, String userId, String role, String refreshToken, Instant expiresAt) {
		super();
		this.token = token;
		this.userId = userId;
		this.role = role;
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}
}
