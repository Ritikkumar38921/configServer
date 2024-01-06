package com.ritik.lms.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.ritik.lms.entity.User;
import com.ritik.lms.event.OnRegistrationCompleteEvent;
import com.ritik.lms.service.AuthService;
import com.ritik.lms.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private AuthService authService;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, Object> request) {
		try {
			String userName = (String) request.get("userName");
			String password = (String) request.get("password");
			Map<String, Object> body = authService.login(userName, password);
			return ResponseEntity.ok(body);
		} catch (Exception e) {
			logger.error("", e);
			return ResponseEntity.internalServerError().body("Server Error");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Map<String, Object> request) {
		try {
			String userName = request.get("userName").toString();
			String email = request.get("email").toString();
			String password = request.get("password").toString();
			User user = userService.createUser(userName, email, password);
			
			applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, "http://localhost:8082"));
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			logger.error("", e);
			return ResponseEntity.internalServerError().body("Server Errror");
		}
	}
	
	@GetMapping("/confirmRegistration")
	public ResponseEntity<?> confirmRegistration( @RequestParam("token") String token){
		try {
			authService.confirmRegistration(token);
			return ResponseEntity.ok("Confirm Registration SuccessFully");
		} catch (Exception e) {
			logger.error("", e);
			return ResponseEntity.internalServerError().body("Server Errror");
		}
	}

}
