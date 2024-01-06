package com.ritik.lms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

	@Value("${lms.smtp.from}")
	private String from;

	@Value("${lms.smtp.password}")
	private String password;

	@Value("${lms.smtp.port}")
	private String port;

	@Value("${lms.smtp.host}")
	private String host;

	@Value("${lms.smtp.protocols}")
	private String protocols;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getProtocols() {
		return protocols;
	}

	public void setProtocols(String protocols) {
		this.protocols = protocols;
	}

}
