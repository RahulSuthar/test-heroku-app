package com.smallbazaar.rest.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smallbazaar.rest.dto.AuthenticationResponse;
import com.smallbazaar.rest.dto.LoginRequest;
import com.smallbazaar.rest.dto.RefreshTokenRequest;
import com.smallbazaar.rest.dto.RegisterRequest;
import com.smallbazaar.rest.exceptions.EntityNotFoundException;
import com.smallbazaar.rest.exceptions.UserAlreadyExistException;
import com.smallbazaar.rest.model.User;
import com.smallbazaar.rest.model.VerificationToken;
import com.smallbazaar.rest.repository.UserRepository;
import com.smallbazaar.rest.repository.VerificationTokenRepository;
import com.smallbazaar.rest.utils.Enum.Role;
import com.smallbazaar.rest.utils.JwtTokenUtil;

@Service
public class AuthService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	public void signup(RegisterRequest registerRequest) throws UserAlreadyExistException {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setRole(Role.USER);
		user.setCreatedAt(Instant.now());
		user.setEnabled(false);

		if (isUsernameAlreadyInUse(registerRequest.getUsername())) {
			throw new UserAlreadyExistException("User already exists with username : " + registerRequest.getUsername());
		}
		userRepository.save(user);

		String token = generateVerificationToken(user);
		/*
		 * mailService.sendMail(new NotificationEmail("Please Activate your Account",
		 * user.getEmail(), "Thank you for signing up to Spring Reddit, " +
		 * "please click on the below url to activate your account : " +
		 * "http://localhost:8080/api/auth/accountVerification/" + token));
		 */
		System.out.println("http://localhost:8080/api/auth/accountVerification/" + token);
	}

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		fetchUserAndEnable(verificationToken
				.orElseThrow(() -> new EntityNotFoundException(VerificationToken.class, "token", token)));
	}

	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new EntityNotFoundException(User.class, "username", username));
		user.setEnabled(true);
		userRepository.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginRequest) throws AuthenticationException {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		User user = userRepository.findByUsername(loginRequest.getUsername()).get();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenUtil.generateToken(user);
		return new AuthenticationResponse(token, user.getUserId(), user.getRole().name(),refreshTokenService.generateRefreshToken().getToken(),Instant.now().plusMillis(jwtTokenUtil.getJwtExpirationInMillis()));
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);

		verificationTokenRepository.save(verificationToken);
		return token;
	}

	public boolean isUsernameAlreadyInUse(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	public AuthenticationResponse refreshTokens(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token=jwtTokenUtil.generateTokenWithUsername(refreshTokenRequest.getUsername());
		return new AuthenticationResponse(token, "", "", refreshTokenService.generateRefreshToken().getToken(), Instant.now().plusMillis(jwtTokenUtil.getJwtExpirationInMillis()));
	}

}
