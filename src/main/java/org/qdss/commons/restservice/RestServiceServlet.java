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
package org.qdss.commons.restservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.qdss.commons.util.log.Log;
import org.qdss.commons.util.log.LogFactory;
import org.qdss.commons.web.ServletUtils;

/**
 * REST服务转发
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: RestServiceServlet.java 102 2012-09-14 16:23:10Z zhaofang123@gmail.com $
 */
public class RestServiceServlet extends HttpServlet {
	private static final long serialVersionUID = -6080451452978705842L;
	
	private static final Log LOG = LogFactory.getLog(RestServiceServlet.class);

	private static final Map<String, Class<RestService>> serviceMap = new HashMap<String, Class<RestService>>();
	
	/**
	 * 注册服务
	 * 
	 * @param restName
	 * @param clazz
	 */
	public static void registerService(String restName, Class<RestService> clazz) {
		if (LOG.isInfoEnabled())
			LOG.info("Register rest service: " + restName + "=" + clazz.getName());
		serviceMap.put(restName, clazz);
	}

	// -----------------------------------------------------------------------------------
	
	/**
	 * 执行服务分配
	 */
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, ?> pMap = req.getParameterMap();
		
		Map<String, String> params = new HashMap<String, String>();
		for (Map.Entry<String, ?> e : pMap.entrySet()) {
			Object v = e.getValue();
			if (v instanceof Object[]) {
				params.put(e.getKey(), ((String[]) v)[0]);
			} else {
				params.put(e.getKey(), (String) e.getValue());
			}
		}

		final String service = params.get("service");
		if (StringUtils.isBlank(service) || !serviceMap.containsKey(service)) {
			ServletUtils.write(resp,
					String.format("status=%d&message=%s",
							RestService.STATUS_ERROR, "No match service found:" + service));
			return;
		}

		Class<?> clazz = serviceMap.get(service);
		RestService instance = (RestService) clazz.newInstance();
		String response = instance.doAction(params);
		ServletUtils.write(resp, response);
	}
	
	// -----------------------------------------------------------------------------------
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {
			execute(req, resp);
		} catch (Exception ex) {
			ServletUtils.write(resp, 
					String.format("status=%d&message=Exception:%s", RestService.STATUS_ERROR, ex));
			LOG.error("rest service error!", ex);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
