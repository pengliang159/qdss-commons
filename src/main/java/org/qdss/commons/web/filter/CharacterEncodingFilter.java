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
import java.nio.charset.Charset;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 编码过滤器
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: CharacterEncodingFilter.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class CharacterEncodingFilter implements Filter {

	private String encoding = "UTF-8";
	private String contentType;
	
	public void init(FilterConfig config) throws ServletException {
		String encoding = config.getInitParameter("encoding");
		if (encoding != null && Charset.isSupported(encoding))
			this.encoding = encoding;
		this.contentType = "text/html;charset=" + this.encoding;
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		response.setContentType(contentType);
		
		chain.doFilter(request, response);
	}
	
	public void destroy() {
	}
}
