package org.qdss.commons.util.log.internal;

import org.qdss.commons.util.log.Log;

/**
 * No logger
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: NullLog.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class NullLog implements Log {

	public boolean isDebugEnabled() {
		return false;
	}
	
	public boolean isInfoEnabled() {
		return false;
	}
	
	public boolean isWarnEnabled() {
		return false;
	}
	
	public void debug(Object o) {
	}

	public void error(Object o) {
	}

	public void error(Object o, Throwable exception) {
	}

	public void info(Object o) {
	}

	public void warn(Object o) {
	}

	public void warn(Object o, Throwable exception) {
	}
}
