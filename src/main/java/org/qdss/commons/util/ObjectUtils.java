/*
 Copyright (C) 2011 QDSS.org. All rights reserved.
 QDSS.org PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.qdss.commons.util;

import java.math.BigDecimal;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 对象处理相关
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: ObjectUtils.java 70 2011-09-29 08:26:42Z zhaofang123@gmail.com
 *          $
 */
public class ObjectUtils {
	
	/**
	 * @param o
	 * @return
	 */
	public static boolean toBool(Object o) {
		return toBool(o, Boolean.FALSE);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see BooleanUtils#toBoolean(String)
	 */
	public static boolean toBool(Object o, boolean defaultVal) {
		if (o == null) {
			return defaultVal;
		} else if (o instanceof Boolean) {
			return (Boolean) o;
		}
		return BooleanUtils.toBoolean(o.toString());
	}

	/**
	 * @param o
	 * @return
	 */
	public static int toInt(Object o) {
		return toInt(o, 0);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see NumberUtils#toInt(String)
	 */
	public static int toInt(Object o, int defaultVal) {
		if (o == null) {
			return defaultVal;
		}
		if (o instanceof Number) {
			return ((Number) o).intValue();
		}
		return NumberUtils.toInt(o.toString());
	}

	/**
	 * @param o
	 * @return
	 */
	public static long toLong(Object o) {
		return toLong(o, 0L);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see NumberUtils#toLong(String)
	 */
	public static long toLong(Object o, long defaultVal) {
		if (o == null) {
			return defaultVal;
		}
		if (o instanceof Number) {
			return ((Number) o).longValue();
		}
		return NumberUtils.toLong(o.toString());
	}

	/**
	 * @param o
	 * @return
	 */
	public static double toDouble(Object o) {
		return toDouble(o, 0d);
	}

	/**
	 * @param o
	 * @param defaultVal
	 * @return
	 * @see NumberUtils#toDouble(String)
	 */
	public static double toDouble(Object o, double defaultVal) {
		if (o == null) {
			return defaultVal;
		}
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}
		return NumberUtils.toDouble(o.toString());
	}
	
	/**
	 * 比较两个对象
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 * @see Object#equals(Object)
	 */
	public static boolean equals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else if (o2 == null) {
			return o1 == null;
		}
		return o1.equals(o2);
	}
	
	/**
	 * 保留指定精度（四舍五入）
	 * 
	 * @param value
	 * @param scale
	 * @return
	 */
	public static double round(double value, int scale) {
		return BigDecimal.valueOf(value)
				.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 除法，两个数字中任意一个为0则返回0
	 * 
	 * @param divisor
	 * @param dividend
	 * @return
	 */
	public static double divide(int divisor, int dividend) {
		return divide(divisor + 0d, dividend + 0d);
	}
	
	/**
	 * 除法，两个数字中任意一个为0则返回0
	 * 
	 * @param divisor
	 * @param dividend
	 * @return
	 */
	public static double divide(double divisor, double dividend) {
		if (divisor == 0 || dividend == 0) {
			return 0d;
		}
		return divisor / dividend;
	}
}
