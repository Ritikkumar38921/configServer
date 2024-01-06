package com.ritik.lms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

	@Value("${lms.jwtSecret}")
	private String jwtSecret;
	
	@Value("${lms.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	@Value("${lms.passwordLinkExpirationMs}")
	private int passwordLinkExpirationMs;

	public String getJwtSecret() {
		return jwtSecret;
	}

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	public int getJwtExpirationMs() {
		return jwtExpirationMs;
	}

	public void setJwtExpirationMs(int jwtExpirationMs) {
		this.jwtExpirationMs = jwtExpirationMs;
	}

	public int getPasswordLinkExpirationMs() {
		return passwordLinkExpirationMs;
	}

	public void setPasswordLinkExpirationMs(int passwordLinkExpirationMs) {
		this.passwordLinkExpirationMs = passwordLinkExpirationMs;
	}
	
}
