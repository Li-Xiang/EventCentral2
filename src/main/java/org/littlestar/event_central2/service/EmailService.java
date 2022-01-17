package org.littlestar.event_central2.service;

import java.util.List;

import org.littlestar.event_central2.service.EmailServiceImpl.InputStreamSourceAttachement;

public interface EmailService {
	/**
	 * 发送简单邮件
	 * @param from 发件人地址
	 * @param to 收件人地址
	 * @param subject  邮件标题
	 * @param content 邮件内容
	 */
	public void sendMail(String from, String[] to, String subject, String content) throws Exception;
	
	/**
	 * 发送带附件的邮件
	 * @param from 发件人地址
	 * @param to 收件人地址
	 * @param subject  邮件标题
	 * @param content 邮件内容
	 * @param attachments 附件列表
	 */
	public void sendMail(String from, String[] to, String subject, String content, List<InputStreamSourceAttachement> attachments) throws Exception;
	
	//public void sendMail(String to, String subject, String content, List<FileAttachment> attachments) throws Exception;
}
