package org.littlestar.event_central2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.littlestar.event_central2.listener.SnmpTrapListener;
import org.littlestar.event_central2.listener.SyslogListener;
import org.littlestar.event_central2.observer.EventDbLogger;
import org.littlestar.event_central2.observer.EventSmtpLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ListenerRunner implements ApplicationRunner {
	final List<EventListener> listeners = new Vector<EventListener>();
	private static final Logger LOGGER = LoggerFactory.getLogger(ListenerRunner.class);
	@Autowired EventDbLogger dbLogger;
	@Autowired EventSmtpLogger smtpLogger;  
	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			SnmpTrapListener snmpEventListener = new SnmpTrapListener();
			snmpEventListener.addEventObserver(dbLogger);
			snmpEventListener.addEventObserver(smtpLogger);
			snmpEventListener.startup();
			listeners.add(snmpEventListener);
			LOGGER.info(snmpEventListener.getName() + "监听启动...");
			
			SyslogListener syslogListener = new SyslogListener();
			syslogListener.addEventObserver(dbLogger);
			syslogListener.addEventObserver(smtpLogger);
			syslogListener.startup();
			listeners.add(syslogListener);
			LOGGER.info(syslogListener.getName() + "监听启动...");
		} catch (Exception e) {
			LOGGER.error("事件监听启动失败!", e);
			System.exit(-1);
		}
	}

	public List<ListenerInfo> getListenerInfo() {
		List<ListenerInfo> info = new ArrayList<ListenerInfo>();
		for (EventListener l : listeners) {
			ListenerInfo li = new ListenerInfo();
			li.setName(l.getName());
			li.setProtocol(l.getProtocol());
			li.setPort(l.getPort());
			li.setListening(l.isListening());
			info.add(li);
		}
		return info;
	}

	class ListenerInfo {
		private String name;
		private String protocol;
		private int port;
		private boolean listening;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public boolean isListening() {
			return listening;
		}

		public void setListening(boolean listening) {
			this.listening = listening;
		}

		@Override
		public String toString() {
			return name + ": protocol=" + getProtocol() + "; port=" + getPort() + "; listening=" + isListening();
		}
	}
}
