package com.ritik.lms.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ritik.lms.entity.User;
import com.ritik.lms.entity.VerificationToken;
import com.ritik.lms.service.EmailService;
import com.ritik.lms.service.JwtService;
import com.ritik.lms.service.TokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private EmailService emailService;

	@Autowired
	@Qualifier("htmlTemplateEngine")
	private SpringTemplateEngine templateEngine;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		try {
			User user = event.getUser();
			String token = jwtService.generateEmailConfirmationToken(user.getUserName());
			VerificationToken verificationToken = tokenService.save(user.getUserName(), token);
			List<String> toList = Arrays.asList(user.getEmail());
			String url = event.getUrl();
			url = url.concat("/confirmRegistration?token=").concat(verificationToken.getToken());
			Map<String, Object> data = new HashMap<>();
			data.put("userName", user.getUserName());
			data.put("source", "LMS Software Product");
			data.put("url", url);
			String body = templateEngine.process("confirmRegistration.html", new Context(Locale.getDefault(), data));
			emailService.sendEmail(toList, new ArrayList<>(), new ArrayList<>(), "Confirm Registration", body,
					new HashMap<>());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
