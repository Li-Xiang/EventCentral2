package org.littlestar.event_central2;

import org.littlestar.event_central2.entity.Event;

public interface EventObserver {
	public void eventReceived(Event event);
	public void close();
}