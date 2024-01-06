package com.ritik.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ritik.lms.db.UserDb;
import com.ritik.lms.entity.User;

import jakarta.transaction.Transactional;

@Component
public class MyUserDetail implements UserDetailsService {

	@Autowired
	private UserDb userDb;

	@Autowired
	private UserDetail userDetail;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDb.findByEnabledUser(username,"YES");
		if (user == null) {
			throw new UsernameNotFoundException("User not found " + username);
		}

		return userDetail.build(user);
	}

}
