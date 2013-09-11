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

import junit.framework.TestCase;

/**
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-10-5
 * @version $Id: CodecUtilsTest.java 99 2012-09-07 17:05:19Z zhaofang123@gmail.com $
 */
public class CodecUtilsTest extends TestCase {

	public void testAny2Dec() throws Exception {
		for (int i = 0; i < 10000000; i++) {
			String any = CodecUtils.dec2any(i, 62);
			System.out.println(i + " = " + any + ", " + CodecUtils.any2dec(any, 62));
		}
	}
}
