package com.ritik.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.ritik.lms.entity.VerificationToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogoutService implements LogoutHandler {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private TokenService tokenService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String jwt = jwtService.parseJwt(request);
		VerificationToken verificationToken = tokenService.findByToken(jwt);
		if (verificationToken != null) {
			tokenService.deleteToken(jwt);
			SecurityContextHolder.clearContext();
		}
	}

}
