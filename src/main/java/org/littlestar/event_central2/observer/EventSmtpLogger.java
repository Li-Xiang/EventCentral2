package org.littlestar.event_central2.observer;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.littlestar.event_central2.EventObserver;
import org.littlestar.event_central2.entity.Event;
import org.littlestar.event_central2.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventSmtpLogger implements EventObserver {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventSmtpLogger.class);
	final SmtpWriter smtpWriter;
    
	@Value("${spring.mail.properties.from:user@example.com}") 
    private String from;
    
	@Value("#{'${spring.mail.properties.to:}'.split(';')}")
	private String[] to;
	
	public EventSmtpLogger() {
		smtpWriter = new SmtpWriter();
		smtpWriter.start();
	}

	@Override
	public void eventReceived(Event event) {
		smtpWriter.put(event);
	}

	@Override
	public void close() {
		smtpWriter.shutdown();
	}

	@Autowired private EmailService emailService;
	class SmtpWriter extends Thread {
		private volatile boolean running = true;
		private final BlockingQueue<Event> writeQueue = new LinkedBlockingQueue<Event>(500);

		public SmtpWriter() {
			setName("smtp-writer-thread-" + getId());
		}

		public void put(Event event) {
			try {
				if (!Objects.isNull(event))
					writeQueue.put(event);
			} catch (Exception e) {
				LOGGER.error("事件加入smtp写队列失败!", e);
			}
		}

		@Override
		public void run() {
			while (running) {
				try {
					Event event = writeQueue.take();
					String subject = event.getDigest();
					String content = "事件摘要信息:\n" + subject + "\n\n事件详细数据: \n" + event.getData();
					emailService.sendMail(from, to, subject, content);
				} catch (Exception e) {
					LOGGER.error("事件smpt发送失败!", e);
				}
			}
		}

		public void shutdown() {
			running = false;
		}

	}
}
