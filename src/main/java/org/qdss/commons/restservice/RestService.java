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

import java.io.Serializable;
import java.util.Map;

/**
 * REST服务接口
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: RestService.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public interface RestService extends Serializable {

	/**
	 * 正常状态
	 */
	final int STATUS_OK = 1;

	/**
	 * 失败状态
	 */
	final int STATUS_FAIL = 0;

	/**
	 * 错误状态，处理时发生异常
	 */
	final int STATUS_ERROR = 10;

	/**
	 * 处理请求
	 * 
	 * @param parameters
	 * @return
	 */
	public String doAction(Map<String, String> parameters);
}
