package com.ritik.lms.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ritik.lms.entity.VerificationToken;

public interface VerificationTokenDb extends JpaRepository<VerificationToken, Long> {

	public VerificationToken findByToken(String token);

	@Modifying
	@Query("delete from VerificationToken where token = ?1")
	public void deleteByToken(String token);

	@Modifying
	@Query("delete from VerificationToken v where v.user.userName = ?1")
	public void deleteUserName(String userName);

}
