package com.ritik.lms.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ritik.lms.entity.User;

@Component
public class UserDetail implements UserDetails {

	private Long id;
	private String username;
	private String password;
	private String email;
	private boolean enabled;

	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetail() {}

	public UserDetail(Long id, String username, String password, String email, boolean enabled,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.authorities = authorities;
	}

	public static UserDetail build(User user) {
		List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_USER");
		List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
		return new UserDetail(user.getId(), user.getUserName(), user.getPassword(), user.getEmail(),
				user.getEnabled() == "NO" ? false : true, authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
