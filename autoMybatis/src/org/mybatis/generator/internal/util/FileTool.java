package org.mybatis.generator.internal.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wayne E-mail: wxbpxm@163.com
 * @version
 */
public class FileTool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 String path = "/Users/wangxuebiao/Downloads/Mobile.csv";
		 List<String> tmpList = readFile(path);
		 for(String tmp : tmpList) {
			 String [] ary = tmp.split(",");
			 if(ary[1].startsWith("173")) {
				 System.err.println(tmp);
			 }
		 }
		 
	}
	
	public static void addServiceConfig(String path,String configContent,String comment){
		String content = readFileToString(path);
		
		StringBuilder newConfig =new StringBuilder("\t<!-- ")
			.append(comment).append(" -->\n")
			.append("\t").append(configContent).append("\n");
		content = content.replace("</beans>", newConfig.toString()+"</beans>");
		FileTool.writeToFile(content, path);
	}

	public static String getCopyright() {
		StringBuilder sb = new StringBuilder();
		sb.append("/*\n");
		sb.append("* Copyright (c) 2012-2013, Yunnan Yuan Xin technology Co., Ltd.\n");
		sb.append("*\n");
		sb.append("* All rights reserved.\n");
		sb.append("*/");
		return sb.toString();
	}

	// to read a file to list line by line
	public static List<String> readFile(String path) {
		ArrayList<String> retList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			FileInputStream fis = new FileInputStream(path);
			br = new BufferedReader(new InputStreamReader(fis,
					"GBK"));
			String temp = null;

			while ((temp = br.readLine()) != null) {
				retList.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return retList;
	}
	
	// to read a file to list line by line
    public static String readFileToString(String path) {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = null;
			try {
				FileInputStream fis = new FileInputStream(path);
				br = new BufferedReader(new InputStreamReader(fis,
						"UTF-8"));
				String temp = null;

				while ((temp = br.readLine()) != null) {
					sb.append(temp).append("\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return sb.toString();
		}

	// to read a init config file to Map line by line, the map key mapped the
	// config key, the map value mapped the config value.
	public static Map<String, String> readInitFile(String path) {
		Map<String, String> retMap = new HashMap<String, String>();
		BufferedReader br = null;
		try {
			FileInputStream fis = new FileInputStream(path);
			br = new BufferedReader(new InputStreamReader(fis,
					"GB2312"));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				if (temp.trim().isEmpty())
					continue;
				if (!temp.contains("="))
					continue;
				if (temp.startsWith("="))
					continue;
				if (temp.endsWith("=")) {
					String[] str = temp.split("=");
					String key = str[0];
					String value = "";
					retMap.put(key, value);
				} else {
					String[] str = temp.split("=");
					String key = str[0];
					String value = str[1];
					retMap.put(key, value);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return retMap;
	}

	public static void writeToFile(String content, String path) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path, false), "UTF-8"));
			bw.write(content);
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeToFile(List<String> contentLst, String path) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path, false), "UTF-8"));
			for(String tmp : contentLst){
				bw.write(tmp);
				bw.write("\n");
			}
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean fileExist(String path) {
		File file = new File(path);
		return file.exists();
	}
}
