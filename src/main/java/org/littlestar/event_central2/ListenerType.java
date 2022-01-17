package org.littlestar.event_central2;

public enum ListenerType {
	SNMP(0), SYSLOG(1);
	private final int code;
	ListenerType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static ListenerType getType(final int code) {
		switch (code) {
		case 0:
			return SNMP;
		case 1:
			return SYSLOG;
		}
		return null;
	}
	
	public static ListenerType getType(final String p) {
		if(SNMP.isEqual(p))
			return SNMP;
		if(SYSLOG.isEqual(p))
			return SYSLOG;
		return null;
	}

	public boolean isEqual(final String name) {
		return name().equalsIgnoreCase(name);
	}
}

