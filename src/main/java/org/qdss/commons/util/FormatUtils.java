/*
 Copyright (C) 2012 QDSS.org
 
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

import java.text.DecimalFormat;

/**
 * 格式化工具
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @date 2011-4-10
 * @version $Id: FormatUtils.java 101 2012-09-11 15:24:46Z zhaofang123@gmail.com $
 */
public class FormatUtils extends DateFormatUtils {

	/**
	 * @param number
	 * @return
	 */
	public static String formatNumber(Number number) {
		return new DecimalFormat(",###").format(number);
	}
	
	/**
	 * @param number
	 * @return
	 */
	public static String formatDecimal(Number number) {
		return new DecimalFormat(",##0.00").format(number);
	}
	
	static char[] S1 = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
	static char[] S4 = {'分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万'};
	/**
	 * 转换为大写
	 * 
	 * @param money
	 * @return
	 */
	public static String toChineseCurrency(double money) {
		boolean isNegative = money < 0;
		if (isNegative) {
			money = Math.abs(money);
		}
		
		String str = String.valueOf(Math.round(money * 100 + 0.00001));
		String chs = "";
		
		for (int i = 0; i < str.length(); i++) {
			int n = str.charAt(str.length() - 1 - i) - '0';
			chs = S1[n] + "" + S4[i] + chs;
		}

		chs = chs.replaceAll("零仟", "零");
		chs = chs.replaceAll("零佰", "零");
		chs = chs.replaceAll("零拾", "零");
		chs = chs.replaceAll("零亿", "亿");
		chs = chs.replaceAll("零万", "万");
		chs = chs.replaceAll("零元", "元");
		chs = chs.replaceAll("零角", "零");
		chs = chs.replaceAll("零分", "零");

		chs = chs.replaceAll("零零", "零");
		chs = chs.replaceAll("零亿", "亿");
		chs = chs.replaceAll("零零", "零");
		chs = chs.replaceAll("零万", "万");
		chs = chs.replaceAll("零零", "零");
		chs = chs.replaceAll("零元", "元");
		chs = chs.replaceAll("亿万", "亿");

		chs = chs.replaceAll("零$", "");
		chs = chs.replaceAll("元$", "元整");

		return isNegative ? "负" + chs : chs;
	}
}
