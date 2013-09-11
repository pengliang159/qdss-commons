/*
 Copyright (C) 2010 QDSS.org
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.qdss.commons.web.struts;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.qdss.commons.util.ThrowableUtils;
import org.qdss.commons.util.log.Log;
import org.qdss.commons.util.log.LogFactory;
import org.qdss.commons.web.RequestContext;
import org.qdss.commons.web.RequestExecutor;

/**
 * 统一的Action父类，便于统一处理
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: BaseAction.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public abstract class BaseAction extends Action implements RequestExecutor {
	private static final long serialVersionUID = -2008086463646100509L;
	
	private static final Log LOG = LogFactory.getLog(BaseAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		RequestContext context = createRequestContext(mapping, form, request, response);
		try {
			long start = 0;
			if (LOG.isDebugEnabled()) {
				LOG.debug("Executing request: " + context);
				start = System.currentTimeMillis();
			}
			
			// -----
			execute(context);
			// -----
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Execute completed. Time of is: " + (System.currentTimeMillis() - start) + " ms.");
			}
			
		} catch (Exception ex) {
			handleException(context, ex);
		}
		return null;
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected RequestContext createRequestContext(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return new StrutsRequestContextImpl(request, response, mapping, form);
	}
	
	/**
	 * Logging Exception
	 */
	public void handleException(RequestContext context, Throwable ex) {
		Throwable cause = getRootCause(ex);
		LOG.error("Execute request failure!!!", cause);
		
		if (cause instanceof RuntimeException) {
			throw (RuntimeException) cause;
		}
		throw new RuntimeException(cause);
	}
	
	/**
	 * @param cause
	 * @return
	 */
	protected Throwable getRootCause(Throwable cause) {
		Throwable rootCause = null;
		if (cause == null)
			rootCause = new RuntimeException("未知错误！");
		else if (cause instanceof InvocationTargetException)
			rootCause = ((InvocationTargetException) cause).getTargetException();
		else
			rootCause = ThrowableUtils.getRootCause(cause);
		
		return rootCause;
	}
}
