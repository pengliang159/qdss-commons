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
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-10-5
 * @version $Id: EncryptUtilsTest.java 62 2011-05-15 16:25:15Z zhaofang123 $
 */
public class EncryptUtilsTest extends TestCase {

	public void testCRC32() throws Exception {
		System.out.println(EncryptUtils.toCRC32Hex("406276067".getBytes()));
		System.out.println(EncryptUtils.toCRC32Hex("01234567891".getBytes()));
		
		for (long i = 0; i < Integer.MAX_VALUE; i++) {
			System.out.println(EncryptUtils.toCRC32HexPadding(("" + i).getBytes()));
		}
	}
}
