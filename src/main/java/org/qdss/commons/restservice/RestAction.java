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

/**
 * 服务动作
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: RestAction.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public abstract class RestAction {
	
	private String service;
	private Map<String, String> parameters;
	
	/**
	 * @param service
	 */
	public RestAction(String service) {
		this(service, new HashMap<String, String>());
	}
	
	/**
	 * @param service
	 * @param parameters
	 */
	public RestAction(String service, Map<String, String> parameters) {
		this.service = service;
		this.parameters = parameters;
	}
	
	/**
	 * @return
	 */
	public String getServiceName() {
		return service;
	}
	
	/**
	 * @param parameters
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * @return
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public Map<String, String> addParameter(String key, String value) {
		if (parameters == null)
			parameters = new HashMap<String, String>();
		parameters.put(key, value);
		return parameters;
	}
	
	/**
	 * @return
	 */
	abstract public ActionResponse sendAction();
}
