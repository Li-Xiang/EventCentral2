package org.littlestar.event_central2;

public enum Severity {
	EMERG(0), ALERT(1), CRITICAL(2), ERROR(3), WARNING(4), NOTICE(5), INFO(6), DEBUG(7);
	
	private final int code;
	
	private Severity(final int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static Severity getSeverity(final int code) {
		switch (code) {
		case 0: return EMERG;
		case 1: return ALERT;
		case 2: return CRITICAL;
		case 3: return ERROR;
		case 4: return WARNING;
		case 5: return NOTICE;
		case 6: return INFO;
		case 7: return DEBUG;
		}
		return INFO;
	}

	public static Severity getSeverity(final String s) {
		if (EMERG.isEqual(s))    return EMERG;
		if (ALERT.isEqual(s))    return ALERT;
		if (CRITICAL.isEqual(s)) return CRITICAL;
		if (ERROR.isEqual(s))    return ERROR;
		if (WARNING.isEqual(s))  return WARNING;
		if (NOTICE.isEqual(s))   return NOTICE;
		if (INFO.isEqual(s))     return INFO;
		if (DEBUG.isEqual(s))    return DEBUG;
		return INFO;
	}

	public boolean isEqual(final String name) {
		return name().equalsIgnoreCase(name);
	}
}