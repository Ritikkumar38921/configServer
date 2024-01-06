package com.ritik.lms.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ritik.lms.config.EmailConfig;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import jakarta.mail.internet.MimeMultipart;

@Service
public class EmailService {

	public static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private EmailConfig emailConfig;

	public void sendEmail(List<String> toList, List<String> ccList, List<String> bccList, String subject, String body,
			Map<String, FileDataSource> attachments) throws AddressException, MessagingException {

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.host", emailConfig.getHost());
		properties.put("mail.smtp.port", emailConfig.getPort());
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.protocols", emailConfig.getProtocols());
		properties.put("mail.smtp.starttls.required", "true");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailConfig.getFrom(), emailConfig.getPassword());
			}
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(emailConfig.getFrom()));
		message.setSubject(subject);
		message.setSentDate(new Date());

		for (String to : toList) {
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
		}

		for (String cc : ccList) {
			message.setRecipient(RecipientType.CC, new InternetAddress(cc));
		}

		for (String bcc : bccList) {
			message.setRecipient(RecipientType.BCC, new InternetAddress(bcc));
		}

		MimeBodyPart mimeBodyPart = new MimeBodyPart();

		mimeBodyPart.setText(body, "UTF-8", "html");

		Multipart multipart = new MimeMultipart();

		multipart.addBodyPart(mimeBodyPart);

		attachments.entrySet().forEach(attachment -> {
			try {
				MimeBodyPart bodyPart = new MimeBodyPart();
				bodyPart.setDataHandler(new DataHandler(attachment.getValue()));
				bodyPart.setFileName(attachment.getKey());
				multipart.addBodyPart(bodyPart);
			} catch (MessagingException e) {
				logger.error("", e);
				throw new RuntimeException(e);
			}
		});

		message.setContent(multipart);
		Transport.send(message);
		logger.info("Send SuccessFully");

	}

}
