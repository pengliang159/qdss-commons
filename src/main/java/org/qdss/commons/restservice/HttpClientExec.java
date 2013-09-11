/*
 Copyright (C) 2009 QDSS.org
 
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.qdss.commons.util.log.Log;
import org.qdss.commons.util.log.LogFactory;

/**
 * Http调用
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: HttpClientFetcher.java 78 2012-01-17 08:05:10Z
 *          zhaofang123@gmail.com $
 */
public class HttpClientExec {

	private static final Log LOG = LogFactory.getLog(HttpClientExec.class);
	
	public static final int MAX_CONNECTIONS_NUMBER = 128;
	public static final int CONNECTION_GET_TIMEOUT = 60 * 1000;
	public static final int READ_DATA_TIMEOUT = 60 * 1000;

	private String encoding;
	private boolean gzip = true;
	
	private HttpClient httpClient;
	
	/**
	 * @param encoding
	 */
	private HttpClientExec(String encoding) {
		LOG.info("Initializing HttpClient ....");
		this.encoding = encoding;

		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setConnectionManagerTimeout(CONNECTION_GET_TIMEOUT);
		clientParams.setSoTimeout(READ_DATA_TIMEOUT);
		clientParams.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, getEncoding());
		clientParams.setConnectionManagerClass(MultiThreadedHttpConnectionManager.class);
		this.httpClient = new HttpClient(clientParams);

		HttpConnectionManagerParams connectionParams = httpClient.getHttpConnectionManager().getParams();
		connectionParams.setDefaultMaxConnectionsPerHost(MAX_CONNECTIONS_NUMBER);
		connectionParams.setMaxTotalConnections(MAX_CONNECTIONS_NUMBER);
	}
	
	/**
	 * @param encoding
	 * @param maxConnection
	 */
	public HttpClientExec(String encoding, int maxConnection) {
		this(encoding);
		
		HttpConnectionManagerParams connectionParams = httpClient.getHttpConnectionManager().getParams();
		connectionParams.setDefaultMaxConnectionsPerHost(maxConnection);
		connectionParams.setMaxTotalConnections(maxConnection);
	}

	/**
	 * @return
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}
	
	/**
	 * @return
	 */
	public boolean isGzip() {
		return gzip;
	}

	/**
	 * @param uri
	 * @return
	 */
	public String executePost(String uri) {
		HttpMethod method = new PostMethod(uri);
		return executeMethod(method);
	}

	/**
	 * @param uri
	 * @return
	 */
	public String executeGet(String uri) {
		HttpMethod method = new GetMethod(uri);
		return executeMethod(method);
	}
	
	/**
	 * @param method
	 * @return
	 */
	public String executeMethod(HttpMethod method) {
		return executeMethod(method, isGzip());
	}

	/**
	 * @param method
	 * @return
	 */
	public String executeMethod(HttpMethod method, boolean gzip) {
		if (gzip) {
			method.addRequestHeader("Accept-Encoding", "gzip, deflate");
		}
		
		try {
			int status = httpClient.executeMethod(method);
			if (status != HttpStatus.SC_OK) {
				return "status=" + status;
			}
			return readResponse(method);
		} catch (ConnectionPoolTimeoutException ex) {
			LOG.warn("Timeout for connection get!");
		} catch (IOException ex) { // SocketTimeoutException/read timeout, SocketException/socket reset
			LOG.warn("Timeout for socket read!");
		} catch (Throwable ex) {
			LOG.warn("Unknow error on executed http method!", ex);
		} finally {
			method.releaseConnection();
		}
		return null;
	}

	/**
	 * 读取二进制数据，如图片
	 * 
	 * @param uri
	 * @return
	 */
	public byte[] readBinary(String uri) {
		return readBinary(new GetMethod(uri));
	}

	/**
	 * 读取二进制数据，如图片
	 * 
	 * @param method
	 * @return
	 * 
	 * @see HttpMethodBase#getResponseBody()
	 */
	public byte[] readBinary(HttpMethod method) {
		try {
			int status = httpClient.executeMethod(method);
			if (status != HttpStatus.SC_OK) {
				throw new ExecuteHttpMethodException("Bad http status: " + status);
			}
			
			return method.getResponseBody();
		} catch (IOException ex) { // SocketTimeoutException/read timeout, SocketException/socket reset
			throw new ExecuteHttpMethodException(ex);
		} catch (Throwable ex) {
			if (ex instanceof ExecuteHttpMethodException) {
				throw (ExecuteHttpMethodException) ex;
			}
			throw new ExecuteHttpMethodException("Unknow error on executed http method!", ex);
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * 解析结果集
	 * 
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public String readResponse(HttpMethod method) throws IOException {
		String responseText = null;
		BufferedReader reader = null;
		try {
			InputStream stream = method.getResponseBodyAsStream();
			if (stream.available() > -1) {
				Header encoding = method.getResponseHeader("Content-Encoding");
				if (encoding != null && encoding.getValue().toUpperCase().contains("GZIP")) {
					stream = new GZIPInputStream(stream);
				}
				String enc = StringUtils.defaultIfEmpty(
						method.getParams().getContentCharset(), getEncoding());
				reader = new BufferedReader(new InputStreamReader(stream, enc));
				
				StringBuffer sb = new StringBuffer();
				char[] buffer = new char[1024 * 4];
				int read = -1;
				while ((read = reader.read(buffer)) > 0) {
					char[] chunk = new char[read];
					System.arraycopy(buffer, 0, chunk, 0, read);
					sb.append(chunk);
				}
				responseText = sb.toString();
			}
			
		} catch (IOException ex) {
			throw ex;
		} finally {
			try {
				reader.close();
			} catch (Exception ex){}
			method.releaseConnection();
		}
		return responseText;
	}
	
	// -----------------------------------------------------------------------------------
	
	private static final HttpClientExec _INSTANCE = new HttpClientExec("UTF-8");
	/**
	 * @return
	 */
	public static HttpClientExec getInstance() {
		return _INSTANCE;
	}
}
