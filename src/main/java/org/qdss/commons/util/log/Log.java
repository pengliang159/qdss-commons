package org.qdss.commons.util.log;

/**
 * Log enter
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: Log.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public interface Log {

	boolean isDebugEnabled();
	
	boolean isInfoEnabled();
	
	boolean isWarnEnabled();
	
	void debug(Object message);

	void info(Object message);

	void warn(Object message);

	void warn(Object message, Throwable exception);

	void error(Object message);

	void error(Object message, Throwable exception);
}
