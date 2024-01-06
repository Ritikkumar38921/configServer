package com.ritik.lms.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableWebMvc
public class ApplicationConfig implements ApplicationContextAware {

	private ApplicationContext applicationcontext;

	@Bean(name = "htmlTemplateEngine")
	public SpringTemplateEngine springTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
		springResourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
		springResourceTemplateResolver.setPrefix("classpath:/templates/");
		springResourceTemplateResolver.setSuffix(".html");
		springResourceTemplateResolver.setCharacterEncoding("UTF-8");
		springResourceTemplateResolver.setApplicationContext(applicationcontext);
		templateEngine.setTemplateResolver(springResourceTemplateResolver);
		return templateEngine;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationcontext = applicationContext;
	}

}
