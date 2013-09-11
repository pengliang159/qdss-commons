package org.qdss.commons.util.log.internal;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.qdss.commons.util.log.Log;

/**
 * implements log4j logger
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: Log4jLogger.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class Log4jLogger implements Log, Serializable {
	private static final long serialVersionUID = 7665415944276955446L;

	// The fully qualified name of the Log4jLogger class
	private static final String FQCN = Log4jLogger.class.getName();

	private static final boolean IS12 = Priority.class.isAssignableFrom(Level.class);

	private transient Logger logger = null;

	private String name = null;

	public Log4jLogger() {
	}

	public Log4jLogger(Class<?> clazz) {
		this.name = clazz.getName();
		this.logger = getLogger();
	}

	public void trace(Object message) {
		trace(message, null);
	}

	public void trace(Object message, Throwable ex) {
		if (IS12)
			getLogger().log(FQCN, (Priority) Level.DEBUG, message, ex);
		else
			getLogger().log(FQCN, Level.DEBUG, message, ex);
	}

	public void debug(Object message) {
		debug(message, null);
	}

	public void debug(Object message, Throwable ex) {
		if (IS12)
			getLogger().log(FQCN, (Priority) Level.DEBUG, message, ex);
		else
			getLogger().log(FQCN, Level.DEBUG, message, ex);
	}

	public void info(Object message) {
		info(message, null);
	}

	public void info(Object message, Throwable ex) {
		if (IS12)
			getLogger().log(FQCN, (Priority) Level.INFO, message, ex);
		else
			getLogger().log(FQCN, Level.INFO, message, ex);
	}

	public void warn(Object message) {
		warn(message, null);
	}

	public void warn(Object message, Throwable exception) {
		if (IS12)
			getLogger().log(FQCN, (Priority) Level.WARN, message, exception);
		else
			getLogger().log(FQCN, Level.WARN, message, exception);
	}

	public void error(Object message) {
		error(message, null);
	}

	public void error(Object message, Throwable exception) {
		if (IS12)
			getLogger().log(FQCN, (Priority) Level.ERROR, message, exception);
		else
			getLogger().log(FQCN, Level.ERROR, message, exception);
	}

	public void fatal(Object message) {
		fatal(message, null);
	}

	public void fatal(Object message, Throwable exception) {
		if (IS12)
			getLogger().log(FQCN, (Priority) Level.FATAL, message, exception);
		else
			getLogger().log(FQCN, Level.FATAL, message, exception);
	}

	public final Logger getLogger() {
		if (logger == null)
			logger = Logger.getLogger(name);
		return logger;
	}

	public boolean isTraceEnabled() {
		return getLogger().isDebugEnabled();
	}
	
	public boolean isDebugEnabled() {
		return getLogger().isDebugEnabled();
	}
	
	public boolean isInfoEnabled() {
		return getLogger().isInfoEnabled();
	}
	
	public boolean isWarnEnabled() {
		if (IS12)
			return getLogger().isEnabledFor((Priority) Level.WARN);
		else
			return getLogger().isEnabledFor(Level.WARN);
	}
	
	public boolean isErrorEnabled() {
		if (IS12)
			return getLogger().isEnabledFor((Priority) Level.ERROR);
		else
			return getLogger().isEnabledFor(Level.ERROR);
	}
	
	public boolean isFatalEnabled() {
		if (IS12)
			return getLogger().isEnabledFor((Priority) Level.FATAL);
		else
			return getLogger().isEnabledFor(Level.FATAL);
	}
}
