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
package org.qdss.commons.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * JSON Entry and helper.
 * Using {@link Bean2Json}
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Fangfang Zhao</a>
 * @version $Id: Json.java 106 2012-12-24 06:50:51Z zhaofang123@gmail.com $
 */
public class Json implements Serializable {
	private static final long serialVersionUID = -7618723160926131245L;
	
	public static final String EMPTY_ARRYA 	= "[]";
	public static final String EMPTY_JSON 	= "{}";
	
	private static final char DOT = '.';
	
	private Map<String, Object> data = new LinkedHashMap<String, Object>();
	private boolean escapeForce;
	
	/**
	 */
	public Json() {
		this(true);
	}
	
	/**
	 * @param escapeForce
	 */
	public Json(boolean escapeForce) {
		this.escapeForce = escapeForce;
	}
	
	/**
	 * @param name
	 * @param sValue
	 * @return
	 */
	public Json addItem(String name, String sValue) {
		if (sValue == null) {
			return this;
		}
		data.put(name, sValue);
		return this;
	}
	
	/**
	 * @param name
	 * @param oValue
	 * @return
	 */
	public Json addItem(String name, Object oValue) {
		if (oValue == null) {
			return this;
		}
		data.put(name, oValue);
		return this;
	}
	
	/**
	 * @param name
	 * @param sValues
	 * @return
	 */
	public Json addItem(String name, String[] sValues) {
		if (sValues == null) {
			return this;
		}
		data.put(name, sValues);
		return this;
	}
	
	/**
	 * @param name
	 * @param oValues
	 * @return
	 */
	public Json addItem(String name, Object[] oValues) {
		if (oValues == null) {
			return this;
		}
		data.put(name, oValues);
		return this;
	}
	
	/**
	 * @return
	 */
	public String toJson() {
		StringBuilder json = new StringBuilder("{");
		
		boolean first = true;
		for (Iterator<Map.Entry<String, Object>> iter = data.entrySet().iterator(); iter.hasNext(); ) {
			if (first) {
				first = false;
			} else {
				json.append(",");
			}
			
			Map.Entry<String, Object> e = iter.next();
			json.append(e.getKey()).append(":");
			
			Object v = e.getValue();
			if (Object[].class.isAssignableFrom(v.getClass())) {
				Object[] strings = (Object[]) v;
				if (strings.length == 0) {
					json.append(EMPTY_ARRYA);
				} else {
					json.append('[');
					boolean first2 = true;
					for (Object o : strings) {
						if (first2) {
							first2 = false;
						} else {
							json.append(",");
						}
						json.append( wrapJsonValue(o, escapeForce) );
					}
					json.append(']');
				}
			} else {
				json.append( wrapJsonValue(v, this.escapeForce) );
			}
		}
		return json.append("}").toString();
	}
	
	@Override
	public String toString() {
		return toJson();
	}
	
	// -----------------------------------------------------------------------------------------
	
	/**
	 * @param value
	 * @return
	 */
	public static String wrapJsonValue(Object value) {
		return wrapJsonValue(value, true);
	}
	
	/**
	 * @param value
	 * @param escapeForce
	 * @return
	 */
	public static String wrapJsonValue(Object value, boolean escapeForce) {
		if (value == null || value.toString().length() < 1) {
			return "''";
		}
		if (value instanceof String) {
			return '\'' + escape(value) + '\'';
		}
		
		String v = value.toString();
		if (escapeForce 
				&& (isNumber(v) || "true".equals(v) || "false".equals(v))) {
			return v;
		}
		return '\'' + escape(v) + '\'';
	}
	
	/**
	 * @param json
	 * @return
	 */
	public static String escape(Object json) {
		if (json == null) {
			return StringUtils.EMPTY;
		}
		
		String jn = json.toString();
		if (jn.contains("'")) {
			jn = jn.replaceAll("'", "&apos;");
		}
		if (jn.contains("\r\n")) {
			jn = jn.replaceAll("\\r\\n", "\\\\r\\\\n");
		} else if (jn.contains("\n")) {
			jn = jn.replaceAll("\\n", "\\\\n");
		}
		if (jn.contains("\\")) {
			jn = jn.replaceAll("\\\\", "\\\\\\\\");
		}
		return jn;
	}
	
	/**
	 * @param json
	 * @return
	 */
	public static String unescape(Object json) {
		if (json == null) {
			return StringUtils.EMPTY;
		}
		return json.toString().replaceAll("&apos;", "'");
	}
	
	/**
	 * @param jsons
	 * @return
	 */
	@Deprecated
	public static String toJson(Json[] jsons) {
		if (jsons == null || jsons.length <= 0) {
			return EMPTY_ARRYA;
		}
		
		StringBuilder json = new StringBuilder("[");
		json.append( StringUtils.join(jsons, ",") ).append(']');
		return json.toString();
	}
	
	/**
	 * @param array
	 * @return <code>[123,433]</code>
	 */
	public static String toJson(Object[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_ARRYA;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Object v : array) {
			if (v instanceof Object[]) {
				sb.append( toJson((Object[]) v) );
			} else {
				sb.append( wrapJsonValue(v) );
			}
			sb.append(',');
		}
		int lenth = sb.length();
		sb.replace(lenth - 1, lenth, "]");
		return sb.toString();
	}
	
	/**
	 * @param array
	 * @return <code>[[1,2],[32,43]]</code>
	 */
	public static String toJson(Object[][] array) {
		if (array == null || array.length <= 0) {
			return EMPTY_ARRYA;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Object[] a : array) {
			sb.append( toJson(a) ).append(',');
		}
		int lenth = sb.length();
		sb.replace(lenth - 1, lenth, "]");
		return sb.toString();
	}
	
	/**
	 * @param v
	 * @return
	 * 
	 * @see NumberUtils#isDigits(String)
	 * @see NumberUtils#isNumber(String)
	 */
	public static boolean isNumber(String v) {
		if (StringUtils.isEmpty(v)) {
            return false;
        }
		
		if (v.charAt(0) == DOT || v.charAt(v.length() - 1) == DOT) {
        	return false;
        }
		
		int dot = 0;
        for (int i = 0; i < v.length(); i++) {
        	char ch = v.charAt(i);
            if (!Character.isDigit(ch)) {
            	if (ch != DOT) {
            		return false;
            	}
            	
            	dot++;
            	if (dot > 1) {
            		return false;
            	}
            }
        }
        return true;
	}
	
	/**
	 * @param map
	 * @return <code>{a:43,b:31}</code>
	 */
	public static String toJson(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return EMPTY_JSON;
		}
		
		Json json = new Json();
		for (Map.Entry<String, Object> e : map.entrySet()) {
			json.addItem(e.getKey(), e.getValue());
		}
		return json.toJson();
	}
	
	/**
	 * @param keys
	 * @param values
	 * @return <code>[{a:1,b:1}, {a:43,b:31}]</code>
	 */
	public static String toJson(String[] keys, Object[][] values) {
		if (values == null || values.length == 0) {
			return EMPTY_ARRYA;
		}
		
		final int keyLen = keys.length;
		List<String> list = new ArrayList<String>();
		int idx = 0;
		for (Object[] v : values) {
			if (v.length != keyLen) {
				throw new IllegalArgumentException("key and value length not match! index: " + idx);
			}
			idx++;
			
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < keyLen; i++) {
				map.put(keys[i], v[i]);
			}
			list.add(toJson(map));
		}
		
		StringBuilder json = new StringBuilder("[");
		json.append( StringUtils.join(list.iterator(), ",") ).append(']');
		return json.toString();
	}
}
