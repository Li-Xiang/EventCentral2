package org.littlestar.event_central2.observer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.littlestar.event_central2.EventObserver;
import org.littlestar.event_central2.entity.Event;
import org.littlestar.event_central2.repository.EventsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDbLogger implements EventObserver {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventDbLogger.class);
	@Autowired
	EventsRepository eventsRepository;
	final DbWriter dbWriter;

	public EventDbLogger() {
		dbWriter = new DbWriter();
		dbWriter.start();
	}

	@Override
	public void eventReceived(Event event) {
		dbWriter.put(event);
	}

	@Override
	public void close() {
		dbWriter.shutdown();
	}

	class DbWriter extends Thread {
		private volatile boolean running = true;
		private final BlockingQueue<Event> writeQueue = new LinkedBlockingQueue<Event>(500);
		public DbWriter() {
			setName("db-writer-thread-" + getId());
		}
		public void put(Event event) {
			try {
				writeQueue.put(event);
			} catch (Exception e) {
				LOGGER.error("事件加入数据库写队列失败!", e);
			}
		}
		
		@Override
		public void run() {
			while (running) {
				try {
					Event event = writeQueue.take();
					eventsRepository.saveAndFlush(event);
				} catch (Exception e) {
					LOGGER.error("事件写入数据库失败!", e);
				}
			}
		}

		public void shutdown() {
			running = false;
		}
	}
}
