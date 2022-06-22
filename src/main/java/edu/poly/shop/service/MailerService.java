package edu.poly.shop.service;

import javax.mail.MessagingException;

import org.springframework.scheduling.annotation.Scheduled;

import edu.poly.shop.model.MailInfo;

public interface MailerService {

	void send(String to, String subject, String body) throws MessagingException;

	void send(MailInfo mail) throws MessagingException;

	void queue(String to, String subject, String body);

	void queue(MailInfo mail);

	void run();

}
