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

import java.util.Map;

import org.qdss.commons.util.CodecUtils;

/**
 * 基础REST服务
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: BaseRestAction.java 99 2012-09-07 17:05:19Z zhaofang123@gmail.com $
 */
public abstract class BaseRestAction extends RestAction {

	/**
	 * @param service
	 */
	public BaseRestAction(String service) {
		super(service);
	}
	
	/**
	 * @param service
	 * @param parameters
	 */
	public BaseRestAction(String service, Map<String, String> parameters) {
		super(service, parameters);
	}

	@Override
	public ActionResponse sendAction() {
		String restUri = buildRestUrl();
		String responseText = HttpClientExec.getInstance().executeGet(restUri);
		return new ActionResponse(responseText);
	}
	
	/**
	 * @return
	 */
	protected String buildRestUrl() {
		StringBuffer restUrl = new StringBuffer(getRestAddr());
		restUrl.append("?service=").append(getServiceName());
		
		Map<String, String> params = getParameters();
		if (params == null || params.isEmpty())
			return restUrl.toString();
	
		for (Map.Entry<String, String> e : params.entrySet()) {
			restUrl.append("&").append(e.getKey()).append("=").append(CodecUtils.urlEncode(e.getValue()));
		}
		return restUrl.toString();
	}
	
	/**
	 * @return
	 */
	abstract protected String getRestAddr();
}
