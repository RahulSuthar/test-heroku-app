package com.smallbazaar.rest.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smallbazaar.rest.exceptions.EntityNotFoundException;
import com.smallbazaar.rest.model.RefreshToken;
import com.smallbazaar.rest.repository.RefreshTokenRepository;

@Service
@Transactional
public class RefreshTokenService {
	
	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		return refreshTokenRepository.save(refreshToken);
	}
	
	public void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
		.orElseThrow(()-> new EntityNotFoundException(RefreshTokenService.class, "token"));
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
	
	
	
}
