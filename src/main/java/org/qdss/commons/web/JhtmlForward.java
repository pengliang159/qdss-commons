/*
 Copyright (c) 2012 QIDAPP.com. All rights reserved.
 QIDAPP.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.qdss.commons.web;

import java.io.FileNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.qdss.commons.util.ThrowableUtils;

/**
 * 页面转发。如果不想暴露为.jsp页面
 * 
 * @author Zhao Fangfang
 * @version $Id: JhtmlForward.java 110 2013-06-22 09:40:22Z zhaofang123@gmail.com $
 * @since 2.6, 2012-9-14
 */
public class JhtmlForward extends BaseServlet {
	private static final long serialVersionUID = -6821655106468381401L;
	
	private String contextPath;
	private String prefix;
	private String suffix;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		contextPath = config.getServletContext().getContextPath();
		prefix = StringUtils.defaultIfEmpty(
				config.getInitParameter("prefix"), "");
		suffix = StringUtils.defaultIfEmpty(
				config.getInitParameter("suffix"), ".htm");
	}

	public void execute(RequestContext context) throws Exception {
		String toUrl = context.getRequest().getRequestURI();
		toUrl = toUrl.replaceFirst(contextPath, prefix);
		toUrl = toUrl.replace(suffix, ".jsp");
		
		try {
			ServletUtils.forward(
					context.getRequest(), context.getResponse(), toUrl);
		} catch (Exception ex) {
			if (ThrowableUtils.getRootCause(ex) instanceof FileNotFoundException) {
				context.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {
				context.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}
}
