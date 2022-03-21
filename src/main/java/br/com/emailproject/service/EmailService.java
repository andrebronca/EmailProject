package br.com.emailproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.emailproject.model.Email;
import br.com.emailproject.util.LogUtil;

@Stateless
public class EmailService extends Thread {

	private List<Email> emails = new ArrayList<>();
	public static final String HEADER_CONTEXT = "text/html; charset=utf-8";

	public void enviar(Email email) {
		emails.add(email);
		send();
	}

	public void enviar(List<Email> emails) {
		this.emails = emails;
		send();
	}

	private void send() {
		new Thread(this.copy()).start();
	}

	private EmailService copy() {
		EmailService emailService = new EmailService();
		emailService.emails = emails;
		return emailService;
	}

	@Override
	public void run() {
		Properties p = new Properties();
		p.put("mail.smtp.starttls.enable", true);
		p.put("mail.smtp.host", System.getProperty("email-project.mail.smtp.host"));
		p.put("mail.smtp.port", System.getProperty("email-project.mail.smtp.port"));

		Session session = Session.getInstance(p);
		session.setDebug(false);

		for (Email email : emails) {
			try {
				Message m = new MimeMessage(session);
				m.setFrom(new InternetAddress(System.getProperty("email-project.mail.from")));

				if (email.getDestinatario().contains("/")) {
					List<InternetAddress> emailsLocal = new ArrayList<>();
					for (String stringEmail : email.getDestinatario().split("/")) {
						emailsLocal.add(new InternetAddress(stringEmail));
					}
					m.addRecipients(Message.RecipientType.TO, emailsLocal.toArray(new InternetAddress[0]));
				} else {
					InternetAddress para = new InternetAddress(email.getDestinatario());
					m.addRecipient(Message.RecipientType.TO, para);
				}

				m.setSubject(email.getAssunto());
				MimeBodyPart textparte = new MimeBodyPart();
				textparte.setHeader("Content-Type", HEADER_CONTEXT);
				textparte.setContent(email.getTexto(), HEADER_CONTEXT);
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(textparte);
				m.setContent(mp);

				Transport.send(m);

			} catch (MessagingException error) {
				LogUtil.getLogger(EmailService.class).error("Erro ao enviar e-mail: " + error.getMessage());
			}
		}
	}
}
