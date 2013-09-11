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

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 执行Http方法失败
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: ExecuteHttpMethodException.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class ExecuteHttpMethodException extends NestableRuntimeException {
	private static final long serialVersionUID = 1350464686207313368L;

	public ExecuteHttpMethodException(String message) {
		super(message);
	}

	public ExecuteHttpMethodException(Throwable ex) {
		super(ex);
	}

	public ExecuteHttpMethodException(String message, Throwable ex) {
		super(message, ex);
	}
}
