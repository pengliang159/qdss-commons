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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 缓存头过滤器
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: CacheHeadFilter.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class CacheHeadFilter implements Filter {

	// 默认为90天
	private int maxAge = (30 * 24 * 60 * 60) * 3;
	
	private String cacheControl;
	
	public void init(FilterConfig config) throws ServletException {
		String maxAge = config.getInitParameter("maxAge");
		if (maxAge != null) {
			this.maxAge = NumberUtils.toInt(maxAge, this.maxAge);
		}
		this.cacheControl = "public, max-age=" + this.maxAge;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (maxAge > 1) {
			((HttpServletResponse) response).addHeader("Cache-Control", cacheControl);
			((HttpServletResponse) response).addHeader("X-Cache", "org.qdss.commons");
		}
		
		chain.doFilter(request, response);
	}
	
	public void destroy() {
	}
}
