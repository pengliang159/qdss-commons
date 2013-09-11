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
package org.qdss.commons.web.filter;

import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * 认证
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: AuthorizationFilter.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public abstract class AuthorizationFilter implements Filter {
	
	/**
	 */
	final protected static ThreadLocal<Serializable> CURRENT_USER_TL = new ThreadLocal<Serializable>();
	
	/**
	 * @return
	 */
	public Serializable getCurrentUser() {
		Serializable u = getCurrentUserNotThrow();
		if (u == null)
			throw new IllegalStateException("No User found from current thread(invoke)");
		return u;
	}
	
	/**
	 * @return
	 */
	public Serializable getCurrentUserNotThrow() {
		return CURRENT_USER_TL.get();
	}

	public void init(FilterConfig config) throws ServletException { }
	public void destroy() { }
}
