package com.ritik.lms.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ritik.lms.entity.VerificationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TokenService tokenService;

	/// when to save the token in the db

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = jwtService.parseJwt(request);
		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		String username = jwtService.extractUsername(jwt);
		if (username != null && (SecurityContextHolder.getContext().getAuthentication() == null)) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			VerificationToken verificationToken = tokenService.findByToken(jwt);
			if (verificationToken == null) {
				filterChain.doFilter(request, response);
				return;
			}
			boolean isTokenValid = jwtService.isTokenExpired(verificationToken.getToken());
			if (isTokenValid == false) {
				filterChain.doFilter(request, response);
				return;
			}
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
	}

}
