package com.ritik.lms.event;

import org.springframework.context.ApplicationEvent;

import com.ritik.lms.entity.User;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

	private User user;
	private String url;

	public OnRegistrationCompleteEvent(User user, String url) {
		super(user);
		this.user = user;
		this.url = url;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
