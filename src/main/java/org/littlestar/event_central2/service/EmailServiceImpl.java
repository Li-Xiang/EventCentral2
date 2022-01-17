package org.littlestar.event_central2.service;

import java.util.List;
import java.util.Objects;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired(required = false) //允许为空, 如果application.properties没有配置spring.mail.* 
    private JavaMailSender emailSender;
    
	@Override
	public void sendMail(String from, String[] to, String subject, String content) throws Exception {
		if (!Objects.isNull(emailSender)) {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(content);
			emailSender.send(message);
		}
	}

	@Override
	public void sendMail(String from, String to[], String subject, String content, List<InputStreamSourceAttachement> attachments) throws Exception {
		MimeMessage message = emailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    helper.setFrom(from);
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(content);
	    for(InputStreamSourceAttachement attachment: attachments) {
	    	helper.addAttachment(attachment.getFileName(), attachment.getInputStreamSource());
	    }
	    emailSender.send(message);
	}
	
	/*
	class FileAttachment {
		private String fileName;
		private File file;

		public FileAttachment(String fileName, File file) {
			this.fileName = fileName;
			this.file = file;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
	}*/
	
	class InputStreamSourceAttachement {
		private InputStreamSource inputStreamSource;
		private String fileName;

		public InputStreamSource getInputStreamSource() {
			return inputStreamSource;
		}

		public void setInputStreamSource(InputStreamSource inputStreamSource) {
			this.inputStreamSource = inputStreamSource;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}
}
