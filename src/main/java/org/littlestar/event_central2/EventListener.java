package org.littlestar.event_central2;

import org.littlestar.event_central2.entity.Event;

public interface EventListener {
	public void addEventObserver(EventObserver l);
	public void removeEventObserver(EventObserver l);
	public void onEventRecived(Event e);
	public void startup() throws Exception;
	public void shutdown() throws Exception;
	public boolean isListening();
	public String getName();
	public String getProtocol();
	public ListenerType getType();
	public int getPort();
	
}