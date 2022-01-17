package org.littlestar.event_central2;

public enum EventState {
	OPEN(0), CLOSED(1);
	private final int code;

	EventState(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static EventState getState(final int code) {
		switch (code) {
		case 0: return OPEN;
		case 1: return CLOSED;
		}
		return OPEN;
	}

	public static EventState getState(final String s) {
		if (OPEN.isEqual(s))   return OPEN;
		if (CLOSED.isEqual(s)) return CLOSED;
		return EventState.OPEN;
	}

	public boolean isEqual(final String name) {
		return name().equalsIgnoreCase(name);
	}
}
