/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mybatis.generator.internal.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 
 * @author linfeng
 */
public class Util {
	public static String getGetMethodName(String fieldName) {
		String tmp = getCaluName(fieldName);
		return "get" + tmp;
	}

	public static String getSetMethodName(String fieldName) {
		String tmp = getCaluName(fieldName);
		return "set" + tmp;
	}

	public static String getCaluName(String moduleName) {
		if (moduleName == null || moduleName.isEmpty()) {
			return "";
		}
		String first = moduleName.substring(0, 1);
		return moduleName.replaceFirst(first, first.toUpperCase());
	}

	public static void main(String[] args) {
	 
	}
	
	public static URLClassLoader getClassLoader(String classPath){
		URLClassLoader retClsLoader = null;
		try {
			URL url1 = new URL("file:/"+classPath);
	    	URL urls [] = {url1};
	    	retClsLoader = URLClassLoader.newInstance(urls);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			retClsLoader = null;
		}
    	return retClsLoader;
	}
}
