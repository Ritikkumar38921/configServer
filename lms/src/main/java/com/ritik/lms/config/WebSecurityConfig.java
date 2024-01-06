package com.ritik.lms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ritik.lms.service.JwtAuthenticationFilter;
import com.ritik.lms.service.LogoutService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(@Autowired AuthenticationConfiguration authConfig)
			throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, @Autowired UserDetailsService userDetailsService,
			@Autowired LogoutService logoutService, @Autowired JwtAuthenticationFilter jwtAuthenticationFilter)
			throws Exception {
		http.csrf(CsrfConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
						.requestMatchers("/auth/**", "/error").permitAll().requestMatchers("/externalData/getData")
						.permitAll().requestMatchers("/password/**").permitAll().anyRequest().authenticated())
				.authenticationProvider(authenticationProvider(userDetailsService))
				.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(log -> log.logoutUrl("/auth/logout").addLogoutHandler(logoutService).logoutSuccessHandler(
						(request, response, authentication) -> SecurityContextHolder.clearContext()));

		http.httpBasic(Customizer.withDefaults());
		http.cors(Customizer.withDefaults());
		return http.build();
	}
}