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
package org.qdss.commons.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.qdss.commons.util.CalendarUtils;
import org.qdss.commons.util.xml.XmlHelper;

/**
 * Servlet工具集
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: ServletUtils.java 104 2012-10-26 11:38:06Z zhaofang123@gmail.com $
 */
public class ServletUtils {
	
	/**
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static String getParameter(ServletRequest request, String paramName) {
		return getParameter(request, paramName, null);
	}
	
	/**
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	public static String getParameter(ServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return value == null ? defaultValue : value;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(ServletRequest request) {
		return getSession(request, false);
	}

	/**
	 * @param request
	 * @param autoCreate
	 * @return
	 */
	public static HttpSession getSession(ServletRequest request, boolean autoCreate) {
		return ((HttpServletRequest) request).getSession(autoCreate);
	}

	/**
	 * @param request
	 * @param attrName
	 * @return
	 */
	public static Object getSessionAttribute(ServletRequest request, String attrName) {
		return getSessionAttribute(request, attrName, null);
	}
	
	/**
	 * @param request
	 * @param attrName
	 * @param defaultValue
	 * @return
	 */
	public static Object getSessionAttribute(ServletRequest request, String attrName, Object defaultValue) {
		Object value = getSession(request).getAttribute(attrName);
		return value == null ? defaultValue : value;
	}

	/**
	 * @param request
	 * @param attrName
	 * @param value
	 */
	public static void setSessionAttribute(ServletRequest request, String attrName, Object value) {
		if (value == null) {
			getSession(request, false).removeAttribute(attrName);
		} else {
			getSession(request, true).setAttribute(attrName, value);
		}
	}
	
	/**
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null && cookies[0] != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookieName.equals(cookies[i].getName())) {
					return cookies[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String readCookie(HttpServletRequest request, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie == null) {
			return null;
		}
		return cookie.getValue();
	}
	
	/**
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue) {
		addCookie(response, cookieName, cookieValue, 60 * 60 * 24 * 14, null, null);
	}
	
	/**
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param maxAge
	 * @param domain
	 * @param path
	 */
	public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge, String domain, String path) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		if (path != null) {
			cookie.setPath(path);
		}
		response.addCookie(cookie);
	}
	
	/**
	 * @param request
	 * @param response
	 * @param cookieName
	 */
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie == null) {
			return;
		}
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	/**
	 * @param response
	 * @return
	 */
	public static PrintWriter getWriter(HttpServletResponse response) {
		try {
			return response.getWriter();
		} catch (IOException ex) {
			throw new RuntimeException("Could't gets writer of response!", ex);
		}
	}
	
	/**
	 * @param response
	 * @param output
	 */
	public static void write(HttpServletResponse response, String output) {
		getWriter(response).write(output);
	}
	
	/**
	 * @param response
	 * @param output
	 */
	public static void writeJson(HttpServletResponse response, String output) {
		setContentType(response, CT_JSON_RFC4627);
		write(response, output);
	}
	
	/**
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Document getRequestDocument(ServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();
		if (reader == null) {
			return null;
		}
		return XmlHelper.createDocument(reader);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String getRequestString(ServletRequest request) {
		BufferedReader br;
		try {
			br = request.getReader();
			if (br == null) {
				return null;
			}
		} catch (IOException ex) {
			throw new RuntimeException("Could't gets reader of response!", ex);
		}
		
		StringBuffer sb = new StringBuffer();
		try {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Could't read line of reader!", ex);
		}
		return sb.toString();
	}
	
	/**
	 * @param response
	 * @param contentType
	 */
	public static void setContentType(ServletResponse response, String contentType) {
		String encod = response.getCharacterEncoding();
		encod = (StringUtils.isBlank(encod)) ? "UTF-8" : encod;
		response.setContentType(contentType + "; charset=" + encod);
	}
	
	/**
	 * @param request
	 * @param response
	 * @param path
	 */
	public static void forward(ServletRequest request, HttpServletResponse response, String path) {
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Exception ex) {
			throw new RuntimeException("Could't forward to url: " + path, ex);
		}
	}
	
	/**
	 * @param response
	 */
	public static void setNoCacheHeaders(HttpServletResponse response) {
		// Set to expire far in the past.
		response.setHeader("expires", "Sat, 6 May 1995 12:00:00 GMT");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("cache-control", "no-cache,no-store,max-age=0");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("cache-control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("pragma", "no-cache");
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static String getReferer(HttpServletRequest request) {
		return (String) request.getHeader("referer");
	}
	
	/**
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String hv = request.getHeader("x-requested-with");
		return "XMLHttpRequest".equals(hv);
	}
	
	/**
	 * 添加缓存头（到期时间、max-age）
	 * 
	 * @param response
	 * @param minute
	 */
	public static void addCacheHead(HttpServletResponse response, int minute) {
		response.setHeader("Cache-Control", "max-age=" + (minute * 60));
    	response.setDateHeader("Expires", CalendarUtils.add(Calendar.MINUTE, minute).getTime());
    	response.setHeader("X-Cache", "org.qdss.commons.xcache");
	}
	
	// -----------------------------------------------------------------------------------

	public static final String INCLUDE_REQUEST_URI = "javax.servlet.include.request_uri";
	public static final String INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";
	public static final String INCLUDE_SERVLET_PATH = "javax.servlet.include.servlet_path";
	public static final String INCLUDE_PATH_INFO = "javax.servlet.include.path_info";
	public static final String INCLUDE_QUERY_STRING = "javax.servlet.include.query_string";

	public static final String FORWARD_REQUEST_URI = "javax.servlet.forward.request_uri";
	public static final String FORWARD_CONTEXT_PATH = "javax.servlet.forward.context_path";
	public static final String FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";
	public static final String FORWARD_PATH_INFO = "javax.servlet.forward.path_info";
	public static final String FORWARD_QUERY_STRING = "javax.servlet.forward.query_string";

	public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";
	public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
	public static final String ERROR_MESSAGE = "javax.servlet.error.message";
	public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
	public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";

	public static final String JSP_JSP_EXCEPTION = "javax.servlet.jsp.jspException";

	public static final String CT_PLAIN = "text/plain";
	public static final String CT_HTML = "text/html";
	public static final String CT_XML = "text/xml";
	public static final String CT_JS = "text/javascript";
	public static final String CT_JS_RFC4392 = "application/javascript";
	public static final String CT_JSON = CT_JS;
	public static final String CT_JSON_RFC4627 = "application/json";
}
