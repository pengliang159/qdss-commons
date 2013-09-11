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

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">FANGFANG ZHAO</a>
 * @version $Id: ReflectUtils.java 99 2012-09-07 17:05:19Z zhaofang123@gmail.com $
 */
public final class ReflectUtils {
	
	private static final Class<?>[] OBJECT = new Class[] { Object.class };
	private static final Class<?>[] NO_PARAM = new Class[] { };

	private static final Method OBJECT_EQUALS;
	private static final Method OBJECT_HASHCODE;
	static {
		Method eq;
		Method hash;
		try {
			eq = Object.class.getMethod("equals", OBJECT);
			hash = Object.class.getMethod("hashCode", NO_PARAM);
		} catch (Exception e) {
			throw new RuntimeException("Could not find Object.equals() or Object.hashCode()", e);
		}
		OBJECT_EQUALS = eq;
		OBJECT_HASHCODE = hash;
	}

	/**
	 * override {@link Object#equals(Object)} method
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean overridesEquals(Class<?> clazz) {
		Method equals;
		try {
			equals = clazz.getMethod("equals", OBJECT);
		} catch (NoSuchMethodException nsme) {
			return false; // its an interface so we can't really tell anything...
		}
		return !OBJECT_EQUALS.equals(equals);
	}

	/**
	 * override {@link Object#hashCode()} method
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean overridesHashCode(Class<?> clazz) {
		Method hashCode;
		try {
			hashCode = clazz.getMethod("hashCode", NO_PARAM);
		} catch (NoSuchMethodException nsme) {
			return false; // its an interface so we can't really tell anything...
		}
		return !OBJECT_HASHCODE.equals(hashCode);
	}

	/**
	 * load class, using current thread {@link ClassLoader}
	 * 
	 * @param clazzName
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> classForName(String clazzName)
			throws ClassNotFoundException {
		try {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			if (contextClassLoader != null) {
				return contextClassLoader.loadClass(clazzName);
			}
		} catch (Throwable t) {
		}
		return Class.forName(clazzName);
	}

	/**
	 * load class, using caller thread
	 * 
	 * @param clazzName
	 * @param caller
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> classForName(String clazzName, Class<?> caller)
			throws ClassNotFoundException {
		try {
			return Class.forName(clazzName, true, caller.getClassLoader());
		} catch (Throwable e) {
		}
		return classForName(clazzName);
	}
	
	/**
	 * class is <code>public</code>
	 * 
	 * @param clazz
	 * @param member
	 * @return
	 */
	public static boolean isPublicClass(Class<?> clazz, Member member) {
		return Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * class is <code>abstract</code>
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isAbstractClass(Class<?> clazz) {
		int modifier = clazz.getModifiers();
		return Modifier.isAbstract(modifier) || Modifier.isInterface(modifier);
	}

	/**
	 * class is <code>final</code>
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isFinalClass(Class<?> clazz) {
		return Modifier.isFinal(clazz.getModifiers());
	}
	

	/**
	 * field is a "public static final" constant
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}
	
	/**
	 * Gets modifier strings
	 * 
	 * @param mInt
	 * @return
	 */
	public static String getModifierString(int mInt) {
		StringBuilder sb = new StringBuilder(32);
		
		if (Modifier.isPublic(mInt))
			sb.append("public ");
		if (Modifier.isProtected(mInt))
			sb.append("protected ");
		if (Modifier.isPrivate(mInt))
			sb.append("private ");
		if (Modifier.isAbstract(mInt))
			sb.append("abstract ");
		if (Modifier.isStatic(mInt))
			sb.append("static ");
		if (Modifier.isFinal(mInt))
			sb.append("final ");
		if (Modifier.isSynchronized(mInt))
			sb.append("synchronized ");
		if (Modifier.isTransient(mInt))
			sb.append("transient ");
		if (Modifier.isVolatile(mInt))
			sb.append("volatile ");
		if (Modifier.isStrict(mInt))
			sb.append("strictfp ");
		if (Modifier.isNative(mInt))
			sb.append("native ");
		if (Modifier.isInterface(mInt))
			sb.append("interface ");
		return sb.toString().trim();
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static Class<?> getSuperclassGenricType(Class<?> clazz) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType))
			return Object.class;
		
		Type[] genTypes = ((ParameterizedType) genType).getActualTypeArguments();
		if (!(genTypes[0] instanceof Class<?>))
			return Object.class;
		
		return (Class<?>) genTypes[0];
	}
	
	private ReflectUtils() {}
}
