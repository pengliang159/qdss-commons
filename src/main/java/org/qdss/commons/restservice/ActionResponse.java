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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 服务响应
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: ActionResponse.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class ActionResponse {
	
	private String response;
	private Map<String, String> map = new HashMap<String, String>();
	
	/**
	 * @param response
	 */
	public ActionResponse(String response) {
		if (StringUtils.isEmpty(response))
			response = "status=" + RestService.STATUS_ERROR;
		this.response = response;
		
		String r_split[] = response.split("&");
		for (String r : r_split) {
			String kv[] = r.split("=");
			if (kv.length != 2) {
				continue;
			}
			map.put(kv[0].trim(), kv[1]);
		}
	}
	
	/**
	 * @return
	 */
	public String getResponse() {
		return response;
	}
	
	/**
	 * @return
	 */
	public int getStatus() {
		String v = getValue("status");
		return NumberUtils.toInt(v, RestService.STATUS_ERROR);
	}
	
	/**
	 * @return
	 */
	public String getMessage() {
		return getValue("message");
	}
	
	/**
	 * @return
	 */
	public String getSecret() {
		return getValue("secret");
	}
	
	/**
	 * @return
	 */
	public String getValue(String key) {
		return map.get(key);
	}
}
