/*
 Copyright (C) 2011 QIDAPP.com. All rights reserved.
 QIDAPP.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.qdss.commons.restservice;

import org.qdss.commons.util.CodecUtils;

/**
 * @author Zhao Fangfang
 * @version $Id: BaseRestService.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public abstract class BaseRestService implements RestService {
	private static final long serialVersionUID = -4025495917385580578L;

	/**
	 * @param status
	 * @param message
	 * @return
	 */
	protected String buildStatusLine(int status, String message) {
		if (message == null) {
			return String.format("status=%d", status);
		}
		return String.format("status=%d&message=%s", status, CodecUtils.urlEncode(message));
	}
}
