package com.ritik.lms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ritik.lms.db.UserDb;
import com.ritik.lms.entity.User;
import com.ritik.lms.entity.VerificationToken;

@Service
public class AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserDb userDb;
	
	public Map<String,Object> login( String userName , String password ){
		Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(userName, password));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    VerificationToken verificationToken = generateToken(userName);
	    
	    // figureout What present in the UserDetails object
	    
	    Map<String,Object> responseBody = new HashMap<>();
	    responseBody.put("token", verificationToken.getToken());
	    responseBody.put("tokenId", verificationToken.getId());
	    responseBody.put("userName", verificationToken.getUser().getUserName());
	    responseBody.put("email", verificationToken.getUser().getEmail());
	    responseBody.put("userId", verificationToken.getUser().getId());
	    
	    
		return responseBody;
	}
	
	private VerificationToken generateToken(String userName) {
		String token = jwtService.generateToken(userName);
		tokenService.deleteUserName(userName);
		return tokenService.save(userName, token);
	}
	
	public void confirmRegistration(String token) {
		 VerificationToken verificationToken = tokenService.findByToken(token);
		 if(verificationToken == null) {
			 throw new RuntimeException("There is no Token in the DB");
		 }
		 
		 if(verificationToken.getExpirationDate().after(new Date()) == true) {
			 throw new RuntimeException("Token Expired");
		 }
		 
		 User user = verificationToken.getUser();
		 user.setEnabled("YES");
		 userDb.save(user);
	}

}
