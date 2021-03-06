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
package org.qdss.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

/**
 * 日期时间工具
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: CalendarUtils.java 107 2012-12-28 02:42:28Z zhaofang123@gmail.com $
 */
public class CalendarUtils extends DateFormatUtils {
	
	public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
	
	public static final Date DT_20991231 = new GregorianCalendar(2099, 11, 31).getTime();  // 2099.12.31
	
	/**
	 * 获取<tt>Calendar</tt>实例
	 * 
	 * @return
	 */
	public static Calendar getInstance() {
		return Calendar.getInstance(DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
	}
	
	/**
	 * 获取<tt>Calendar</tt>实例
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getInstance(Date date) {
		Calendar cal = getInstance();
		cal.setTime(date);
		return cal;
	}

	/**
	 * 返回当前日期
	 * 
	 * @return
	 */
	public static Date now() {
		return getInstance().getTime();
	}
	
	/**
	 * 在当前日期上添加日
	 * 
	 * @param amount
	 * @return
	 */
	public static Date addDay(int amount) {
		return add(Calendar.DAY_OF_MONTH, amount);
	}
	
	/**
	 * 在当前日期上添加月
	 * 
	 * @param amount
	 * @return
	 */
	public static Date addMonth(int amount) {
		return add(Calendar.MONTH, amount);
	}
	
	/**
	 * 在当前日期上添加指定字段和值
	 * 
	 * @param field {@link Calendar}
	 * @param amount
	 * @return
	 */
	public static Date add(int field, int amount) {
		Calendar cal = getInstance();
		cal.add(field, amount);
		return cal.getTime();
	}
	
	/**
	 * 在指定日期上添加日
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addDay(Date date, int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}
	
	/**
	 * 在指定日期上添加月
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMonth(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}
	
	/**
	 * 在指定日期上添加指定字段和值
	 * 
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date add(Date date, int field, int amount) {
		Calendar cal = getInstance(date);
		cal.add(field, amount);
		return cal.getTime();
	}
	
	/**
	 * 时间归零, 00:00:00
	 * 
	 * @param cal
	 */
	public static void clearTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * 时间最大值, 23:59:59
	 * 
	 * @param cal
	 */
	public static void fullTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
	}
	
	/**
	 * 时间归零, 00:00:00
	 * 
	 * @param date
	 */
	public static Date clearTime(Date date) {
		Calendar cal = getInstance(date);
		clearTime(cal);
		return cal.getTime();
	}
	
	/**
	 * 时间最大值, 23:59:59
	 * 
	 * @param date
	 */
	public static Date fullTime(Date date) {
		Calendar cal = getInstance(date);
		fullTime(cal);
		return cal.getTime();
	}
	
	/**
	 * 是否同月（注意：不检查年）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @see #isSameMonth(Date, Date, boolean)
	 */
	public static boolean isSameMonth(Date date1, Date date2) {
		return isSameMonth(date1, date2, false);
	}
	
	/**
	 * 是否同月
	 * 
	 * @param date1
	 * @param date2
	 * @param absolute
	 * @return
	 */
	public static boolean isSameMonth(Date date1, Date date2, boolean absolute) {
		Calendar cal1 = getInstance(date1);
		Calendar cal2 = getInstance(date2);
		if (!absolute) {
			return cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		}
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
	}
	
	/**
	 * 是否同日（注意：不检查年、月）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @see #isSameDay(Date, Date, boolean)
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		return isSameDay(date1, date2, false);
	}
	
	/**
	 * 是否同日
	 * 
	 * @param date1
	 * @param date2
	 * @param absolute
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2, boolean absolute) {
		Calendar cal1 = getInstance(date1);
		Calendar cal2 = getInstance(date2);
		if (!absolute) {
			return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
		}
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) 
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取当前日期到指定日期的相差天数，如果结束日期小于当前日期将返回负数
	 * 
	 * @param end
	 * @return
	 */
	public static int getDayLeft(Date end) {
		return getDayLeft(now(), end);
	}
	
	/**
	 * 获取开始日期到结束日期的相差天数，如果结束日期小于开始日期将返回负数
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getDayLeft(Date begin, Date end) {
		begin = clearTime(begin);
		end = clearTime(end);
		int ct = begin.compareTo(end);
		if (ct == 0) {
			return 0;
		}
		
		if (ct < 0) {
			Calendar cal = getInstance(begin);
			
			int days = -1;
			while (cal.getTime().compareTo(end) <= 0) {
				days++;
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			return days;
		}
		else /* ct < 0 */ {
			Calendar cal = getInstance(end);
			
			int days = -1;
			while (cal.getTime().compareTo(begin) <= 0) {
				days++;
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			return -days;
		}
	}
	
	/**
	 * 解析日期，支持以下日期格式
	 * <ul>
	 * <li>yyyyMMdd</li>
	 * <li>yyyyMMddHHmmss</li>
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy-MM-dd HH:mm:ss</li>
	 * <li>yyyy年MM月dd日</li>
	 * <li>yyyy年MM月dd日 HH时mm分ss秒</li>
	 * </ul>
	 * 
	 * @param source
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Date parse(String source) throws IllegalArgumentException {
		if (StringUtils.isBlank(source)) {
			throw new IllegalArgumentException("Blank source!");
		}

		int len = source.trim().length();
		if (len == 8) {
			return parse(source, getUTCDateFormat());
		} else if (len == 14) {
			return parse(source, getPlainDateTimeFormat());
		} else if (len == 10) {
			return parse(source, getUTCDateFormat());
		} else if (len == 19) {
			return parse(source, getUTCDateTimeFormat());
		} else if (len == 11) {
			return parse(source, getCNDateFormat());
		} else if (len == 21) {
			return parse(source, getCNDateTimeFormat());
		}
		throw new IllegalArgumentException("Illegal source: " + source);
	}
	
	/**
	 * 解析日期，可指定格式
	 * 
	 * @param source
	 * @param format
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Date parse(String source, DateFormat format)
			throws IllegalArgumentException {
		try {
			return format.parse(source);
		} catch (ParseException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
}
