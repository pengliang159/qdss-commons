/*
 Copyright (C) 2011 QIDAPP.com. All rights reserved.
 QIDAPP.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.qdss.commons.util.runtime;

import org.qdss.commons.util.SystemUtils;

import junit.framework.TestCase;

/**
 * @author Zhao Fangfang
 * @version $Id: RuntimeInformationTest.java 72 2011-10-06 09:55:10Z zhaofang123@gmail.com $
 * @since 2.0, 2011-10-6
 */
public class RuntimeInformationTest extends TestCase {

	public void testGetRuntimeInformation() throws Exception {
		RuntimeInformation information = new RuntimeInformation();
		System.out.println("Total HeapMemory: " + information.getTotalHeapMemory());
		System.out.println("Total HeapMemory Used: " + information.getTotalHeapMemoryUsed());
		System.out.println("Total PermGenMemory: " + information.getTotalPermGenMemory());
		System.out.println("Total PermGenMemory Used: " + information.getTotalPermGenMemoryUsed());
		System.out.println("Total NonHeapMemory: " + information.getTotalNonHeapMemory());
		System.out.println("Total NonHeapMemory Used: " + information.getTotalNonHeapMemoryUsed());
		
		System.out.println("Jvm Arguments: " + information.getJvmInputArguments());
	}
	
	public void testGetMemoryPool() throws Exception {
		for (MemoryInformation info : SystemUtils.getMemoryStatistics(true)) {
			System.out.println(info.getName() + ": " + info.getTotal() + "MB, " + info.getUsed() + "MB");
		}
	}
}
