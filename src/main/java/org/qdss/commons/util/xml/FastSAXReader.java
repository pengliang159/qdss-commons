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
package org.qdss.commons.util.xml;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.io.SAXReader;
import org.qdss.commons.util.log.Log;
import org.qdss.commons.util.log.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * SAXReader 缓存
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: FastSAXReader.java 78 2012-01-17 08:05:10Z zhaofang123@gmail.com $
 */
public class FastSAXReader {
	
	private static final Log LOG = LogFactory.getLog(FastSAXReader.class);
	
	private int initial;
	
	private Queue<XMLReader> xmlReadres = new ConcurrentLinkedQueue<XMLReader>();
	
	public FastSAXReader(int initial) {
		this.initial = initial;
		
		doInitAllCache();
	}
	
	public SAXReader getSAXParser() {
		XMLReader xmlReader = xmlReadres.poll();
		if (xmlReader == null) {
			LOG.warn("No more cache, create new XMLReader ....");
			xmlReader = doCachingOneReader(null);
		}
		
		SAXReader saxReader = new SAXReader(xmlReader);
		saxReader.setMergeAdjacentText(true);
		return saxReader;
	}
	
	public void release(SAXReader reader) {
		try {
			xmlReadres.add(reader.getXMLReader());
		} catch (SAXException e) {
			LOG.warn("Release SAXReader to cache fail!", e);
		}
	}
	
	/*-
	 * Initialize cache
	 */
	private void doInitAllCache() {
		LOG.debug("Initializing " + initial + " SAXReader in cache");
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			
			for (int i = 0; i < initial; i++) {
				doCachingOneReader(factory);
			}
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	private XMLReader doCachingOneReader(SAXParserFactory factory) {
		if (factory == null)
			factory = SAXParserFactory.newInstance();
		
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setFeature("http://xml.org/sax/features/validation", false);
			xmlReadres.add(reader);
			
			return reader;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
