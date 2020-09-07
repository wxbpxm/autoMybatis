package org.mybatis.generator.internal.util;

import java.util.Calendar;
import java.util.Date;

public class CommonDataUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	
	public static String getCopyRight() {
		StringBuilder sb = new StringBuilder();
		sb.append("/*\n").append("* Copyright (c) ").append(getCopyRightTime()).append(" 云南空港航空食品有限公司. All rights reserved.\n")
		.append("* create time ").append(new Date()).append("\n*\n*/");
		return sb.toString();
	}
	
	private static String getCopyRightTime() {
		Calendar cl = Calendar.getInstance();
		String start = ""+cl.get(Calendar.YEAR);
		cl.add(Calendar.YEAR, 5);
		String end = "," + cl.get(Calendar.YEAR);
		return start + end;
	}

}
