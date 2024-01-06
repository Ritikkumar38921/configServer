package com.ritik.lms.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ritik.lms.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {

	@Autowired
	private JwtConfig jwtConfig;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	public String parseJwt(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getJwtSecret());
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date( new Date().getTime() + jwtConfig.getJwtExpirationMs()))
				.signWith(getSignInKey(), SignatureAlgorithm.HS512).compact();
	}
	
	public String generateEmailConfirmationToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 86400000))
				.signWith(getSignInKey(), SignatureAlgorithm.HS512).compact();
	}

}
