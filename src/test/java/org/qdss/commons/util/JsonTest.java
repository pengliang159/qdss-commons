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

import java.util.Date;

import junit.framework.TestCase;

/**
 * 
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @since 0.2, 2010-11-8
 * @version $Id: JsonTest.java 105 2012-12-24 03:35:06Z zhaofang123@gmail.com $
 */
@SuppressWarnings("deprecation")
public class JsonTest extends TestCase {

	public void testIsNumber() throws Exception {
		assertFalse(Json.isNumber("."));
		assertTrue(Json.isNumber("11.22"));
		assertTrue(Json.isNumber("1122"));
		assertFalse(Json.isNumber(".10"));
		assertFalse(Json.isNumber("51."));
	}

	public void testJson() {
		Json json = new Json();
		json.addItem("date", new Date());
		json.addItem("numarray", new String[] { "1", "2", "3" });
		json.addItem("strarray", new String[] { "1'", "'2", "3" });
		json.addItem("objarray", new Object[] { "1'", "'2", 3 });

		System.out.println(json);
	}
	
}
