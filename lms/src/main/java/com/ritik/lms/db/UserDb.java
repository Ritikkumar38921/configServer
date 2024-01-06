package com.ritik.lms.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ritik.lms.entity.User;

public interface UserDb extends JpaRepository<User, Long> {

	User findByUserName(String userName);
	
	User findByEnabledUser(String userName,String enabled);

}
