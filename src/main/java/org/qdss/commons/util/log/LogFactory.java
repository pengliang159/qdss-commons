package org.qdss.commons.util.log;

import org.qdss.commons.util.log.internal.JavaLogger;
import org.qdss.commons.util.log.internal.Log4jLogger;
import org.qdss.commons.util.log.internal.NullLog;

/**
 * Log factory
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: LogFactory.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class LogFactory {

	private static Boolean log4jLoggingAvailable;
	private static Boolean javaLoggingAvailable;
	
	/**
	 * Returns an instance of Log suitable for logging from the given class.
	 * 
	 * @param clazz
	 * @return
	 */
	public static Log getLog(Class<?> clazz) {
		if (log4jLoggingAvailable == null) {
			try {
				Class.forName("org.apache.log4j.Logger");
				log4jLoggingAvailable = Boolean.TRUE;
			} catch (Exception ex) {
				log4jLoggingAvailable = Boolean.FALSE;
			}
		}
		
		if (log4jLoggingAvailable.booleanValue()) {
			return new Log4jLogger(clazz);
		}
		else {
			if (javaLoggingAvailable == null) {
				try {
					Class.forName("java.util.logging.Logger");
					javaLoggingAvailable = Boolean.TRUE;
				} catch (Exception ex) {
					javaLoggingAvailable = Boolean.FALSE;
				}
			}
			
			if (javaLoggingAvailable.booleanValue()) {
				return new JavaLogger(clazz);
			} else {
				return new NullLog();
			}
		}
	}
}
