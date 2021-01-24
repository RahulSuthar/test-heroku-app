package com.smallbazaar.rest.utils;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.smallbazaar.rest.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Value("test-secret-key-jwt")
	private String secret;
	
	@Value("${jwt.expiration.time}")
	private long jwtExpirationInMillis;
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token,Claims::getSubject);
	}
	
	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims=getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}
	
	public String generateToken(User user) {
		return doGenerateToken(user);
	}

	private String doGenerateToken(User user) {
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("id", user.getUserId());
		claims.put("role", user.getRole());
		return Jwts.builder().setClaims(claims)
				.setIssuer("small-bazaar.com")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	public String generateTokenWithUsername(String username) {
		Claims claims = Jwts.claims().setSubject(username);
		return Jwts.builder().setClaims(claims)
				.setIssuer("small-bazaar.com")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public long getJwtExpirationInMillis() {
		return jwtExpirationInMillis;
	}
}
