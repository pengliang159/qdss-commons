package org.qdss.commons.util.log.internal;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.qdss.commons.util.log.Log;

/**
 * implements Java logger
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: JavaLogger.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class JavaLogger implements Log, Serializable {
	private static final long serialVersionUID = -2365178919469910624L;

	private transient final Logger logger;

	public JavaLogger(Class<?> clazz) {
		logger = Logger.getLogger(clazz.getName());
	}
	
	public boolean isDebugEnabled() {
		return true;
	}
	
	public boolean isInfoEnabled() {
		return true;
	}
	
	public boolean isWarnEnabled() {
		return true;
	}

	public void debug(Object message) {
		StackTraceElement ste = getInvokerSTE();
		if (ste != null)
			logger.logp(Level.FINE, ste.getClassName(), ste.getMethodName(), message.toString());
		else
			logger.fine(message.toString());
	}

	public void info(Object message) {
		StackTraceElement ste = getInvokerSTE();
		if (ste != null)
			logger.logp(Level.INFO, ste.getClassName(), ste.getMethodName(), message.toString());
		else
			logger.info(message.toString());
	}

	public void warn(Object message) {
		StackTraceElement ste = getInvokerSTE();
		if (ste != null)
			logger.logp(Level.WARNING, ste.getClassName(), ste.getMethodName(), message.toString());
		else
			logger.warning(message.toString());
	}
	
	public void warn(Object message, Throwable exception) {
		StackTraceElement ste = getInvokerSTE();
		if (ste != null)
			logger.logp(Level.WARNING, ste.getClassName(), ste.getMethodName(), message.toString(), exception);
		else
			logger.log(Level.WARNING, message.toString(), exception);
	}

	public void error(Object message) {
		StackTraceElement ste = getInvokerSTE();
        if (ste != null)
        	logger.logp(Level.SEVERE, ste.getClassName(), ste.getMethodName(), message.toString());
        else
        	logger.severe(message.toString());
	}

	public void error(Object message, Throwable exception) {
		StackTraceElement ste = getInvokerSTE();
        if (ste != null)
            logger.logp(Level.SEVERE, ste.getClassName(), ste.getMethodName(), message.toString(), exception);
        else
        	logger.log(Level.SEVERE, message.toString(), exception);
	}

	private StackTraceElement getInvokerSTE() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		if (stack.length > 2) {
			return stack[2];
		}
		return null;
	}
}
