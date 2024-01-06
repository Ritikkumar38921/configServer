package com.ritik.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ritik.lms.db.UserDb;
import com.ritik.lms.db.VerificationTokenDb;
import com.ritik.lms.entity.VerificationToken;

import jakarta.transaction.Transactional;

@Component
public class TokenService {

	@Autowired
	private VerificationTokenDb verificationTokenDb;
	@Autowired
	private UserDb userDb;
	@Autowired
	private JwtService jwtService;
	
	public VerificationToken findByToken(String token) {
		return verificationTokenDb.findByToken(token);
	}
	
	public void deleteToken(String token) {
		verificationTokenDb.deleteByToken(token);
	}
	
	@Transactional
	public VerificationToken save(String userName , String token) {
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(userDb.findByUserName(userName));
		verificationToken.setExpirationDate(jwtService.extractExpiration(token));
		verificationToken = verificationTokenDb.save(verificationToken);
		return verificationToken;
	}

	@Transactional
	public void deleteUserName(String userName) {
		verificationTokenDb.deleteUserName(userName);
		
	}
	
	
}
