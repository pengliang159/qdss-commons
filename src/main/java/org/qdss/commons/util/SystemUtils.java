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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.qdss.commons.util.runtime.MemoryInformation;
import org.qdss.commons.util.runtime.RuntimeInformation;

/**
 * JVM系统工具
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: SystemUtils.java 99 2012-09-07 17:05:19Z zhaofang123@gmail.com $
 */
public final class SystemUtils {

	private SystemUtils() {}
	
	/**
	 * Execute OS command
	 * 
	 * @param command
	 */
	public static void command(String command) {
		command(command, System.out);
	}

	/**
	 * Execute OS command
	 * 
	 * @param command
	 * @param stream
	 * @return
	 */
	public static void command(String command, PrintStream stream) {
		if (StringUtils.isBlank(command))
			return;
		
		BufferedReader reader = null;
		try {
			Runtime rtx = Runtime.getRuntime();
			Process process = rtx.exec(command);
			reader = new BufferedReader(
					new InputStreamReader( process.getInputStream() ));
			
			if (stream != null) {
				String str;
				while ((str = reader.readLine()) != null) {
					stream.println(str);
				}
			}
			
			try {
				System.out.println("Exit: " + process.waitFor());
			} catch (Exception ex) {
				System.err.println("Processes was interrupted");
				throw ex;
			}
			
		} catch (Exception ex) {
			if (!command.startsWith("CMD /C"))
				command("CMD /C " + command);
			else
				throw new RuntimeException("Error executing commond: " + command, ex);
		} finally {
			try {
				reader.close();
			} catch (Exception ex) {}
		}
	}
	
	private static final int sleepWait = 60;
	private static long gcTriggerTime = 0;
	/**
	 * Execute <i>gc</i>
	 */
	synchronized public static void gc() {
		if (System.currentTimeMillis() - gcTriggerTime < 60 * 1000 /* <1m */)
			return;
		gcTriggerTime = System.currentTimeMillis();
		
		try {
			 System.gc();
			 ThreadPool.waitFor(sleepWait);
//	         Thread.sleep(sleepWait);
	         
	         System.runFinalization();
	         ThreadPool.waitFor(sleepWait);
//	         Thread.sleep(sleepWait);
	         
	         System.gc();
	         ThreadPool.waitFor(sleepWait);
//	         Thread.sleep(sleepWait);
	         
	         System.runFinalization();
	         ThreadPool.waitFor(sleepWait);
//	         Thread.sleep(sleepWait);
	         
		} catch (Exception ex) {
			//...
		} finally {
			gcTriggerTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * Gets the memory for used
	 * 
	 * @return
	 * @deprecated Use {@link #getRuntimeInformation()}
	 */
	@Deprecated
	public static long getUsedMemory() {
		gc();
		long total = Runtime.getRuntime().totalMemory();
		gc();
		long free = Runtime.getRuntime().freeMemory();
		return (total - free);
	}
	
	/**
	 * Gets count of threads
	 * 
	 * @return
	 */
	public static long getThreadCount() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup top = group;
		while (group != null) {
			top = group;
			group = group.getParent();
		}
		
		int estimated_size = top.activeCount() * 2;
		Thread[] slackList = new Thread[estimated_size];
		int actual_size = top.enumerate(slackList);
		return actual_size;
	}
	
	private static RuntimeInformation runtimeInformation;
	/**
	 * 获取运行时信息
	 * 
	 * @return
	 */
	synchronized
	public static RuntimeInformation getRuntimeInformation() {
		if (runtimeInformation == null) {
			if (!org.apache.commons.lang.SystemUtils.isJavaVersionAtLeast(1.5f)) {
				throw new RuntimeException("Java version least 1.5");
			}
			runtimeInformation = new RuntimeInformation();
		}
		return runtimeInformation;
	}

	/**
	 * 内存统计（单位：MB）
	 * 
	 * @return
	 */
	public static List<MemoryInformation> getMemoryStatistics() {
		return getMemoryStatistics(false);
	}
	
	/**
	 * 获取内存统计
	 * 
	 * @return
	 */
	public static List<MemoryInformation> getMemoryStatistics(boolean containsPool) {
		List<MemoryInformation> list = new ArrayList<MemoryInformation>();
		
		final RuntimeInformation memory = new RuntimeInformation();
		list.add(new MemoryInformation() {
			public String getName() { return "Heap"; }
			public long getUsed() { return memory.getTotalHeapMemoryUsed(); }
			public long getTotal() { return memory.getTotalHeapMemory(); }
			public long getFree() { return getTotal() - getUsed(); }
		});
		list.add(new MemoryInformation() {
			public String getName() { return "PermGen"; }
			public long getUsed() { return memory.getTotalPermGenMemoryUsed(); }
			public long getTotal() { return memory.getTotalPermGenMemory(); }
			public long getFree() { return getTotal() - getUsed(); }
		});
		list.add(new MemoryInformation() {
			public String getName() { return "NonHeap"; }
			public long getUsed() { return memory.getTotalNonHeapMemoryUsed(); }
			public long getTotal() { return memory.getTotalNonHeapMemory(); }
			public long getFree() { return getTotal() - getUsed(); }
		});
		
		if (containsPool) {
			list.addAll(memory.getMemoryPoolInformation());
		}
		return Collections.unmodifiableList(list);
	}
	
	/**
	 * dump线程信息
	 * 
	 * @param writer
	 */
    public static void dumpThreads(PrintStream writer) {
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        for (Thread thread : traces.keySet()) {
            writer.print(String.format("\nThread[%s@%d,%d,%s]: (state = %s)",
                    thread.getName(), thread.getId(), thread.getPriority(),
                    thread.getThreadGroup().getName(), thread.getState()));
            for (StackTraceElement stackTraceElement : traces.get(thread)) {
                writer.print("\n\t" + stackTraceElement);
            }
        }
    }
}
