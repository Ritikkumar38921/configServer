package com.ritik.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ritik.lms.db.UserDb;
import com.ritik.lms.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserDb userDb;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public User createUser( String userName, String email ,String password ) {
		User user = new User();
		user.setUserName(userName);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setEnabled("NO");
		user = userDb.save(user);
		return user;
	}
}
