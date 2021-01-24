package com.smallbazaar.rest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smallbazaar.rest.dto.AuthenticationResponse;
import com.smallbazaar.rest.dto.LoginRequest;
import com.smallbazaar.rest.dto.RefreshTokenRequest;
import com.smallbazaar.rest.dto.RegisterRequest;
import com.smallbazaar.rest.exceptions.UserAlreadyExistException;
import com.smallbazaar.rest.service.AuthService;
import com.smallbazaar.rest.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody RegisterRequest registerRequest)
			throws UserAlreadyExistException {
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", HttpStatus.CREATED);
	}

	@GetMapping("/accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@Valid @PathVariable String token) {
		authService.verifyAccount(token);
		return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
	}

	@PostMapping("/login")
	public AuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
	
	@PostMapping("/refresh/token")
	public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return authService.refreshTokens(refreshTokenRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return ResponseEntity.status(HttpStatus.OK).body("Refresh Token deleted successfully!!");
	}
}
