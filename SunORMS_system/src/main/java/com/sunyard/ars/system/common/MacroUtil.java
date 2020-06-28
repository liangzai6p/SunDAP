package com.sunyard.ars.system.common;

/*
 * @MacroUtil.java    2013-9-27 下午4:52:33
 *
 * Copyright (c) 2011 Sunyard System Engineering Co., Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Sunyard System Engineering Co., Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use it 
 * only in accordance with the terms of the license agreement you entered 
 * into with Sunyard.
 */


import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @ClassName: MacroUtil
 * @Description: TODO 宏工具类
 * @Author：陈慧民
 * @Date： 2013-9-27 下午4:52:33 (创建文件的精确时间)
 */
public class MacroUtil {
	public static void main(String[] args) {
		System.out.println(MacroUtil.ParseMacro("#&YYYYMMDD_0_[2]&#"));
	}
	
	
	// /////////////////////////////
	// 解析SQL语句中包含的宏。
	// 宏以#&号开始和以&#结束。
	// #&当日(YYYYMMDD)&# 当天
	// #&当日(YYYY-MM-DD)&# 当天
	// #&[n]日前(YYYYMMDD)&# n日前
	// #&[n]日前(YYYY-MM-DD)&# n日前
	public static String ParseMacro(String sSQLs) {
		int nBegin, nEnd;
		int nCur = 0;
		String sMacro;
		StringBuffer sSQL = new StringBuffer(10);
		sSQL.append(sSQLs);
		while (true) {
			nBegin = sSQL.indexOf("#&", nCur);
			// not found.
			if (nBegin == -1)
				break;
			nEnd = sSQL.indexOf("&#", nBegin);
			if (nEnd == -1)
				break;
			nCur = nEnd;
			sMacro = sSQL.substring(nBegin + 2, nEnd);
			// 解析宏
			if(sMacro.indexOf("YYYYMMDD_0") >= 0){
				int i = sMacro.indexOf('[', 0);
				int j = sMacro.indexOf(']', i);
				String sTemp = sMacro.substring(i + 1, j);
				sTemp = sTemp.trim();
				int n = Integer.parseInt(sTemp);
				String sToday;
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
				Calendar calendar = Calendar.getInstance();
				;
				calendar.add(Calendar.DATE, -n);
				java.util.Date date = calendar.getTime();
				StringBuffer buftmp = new StringBuffer(10);
				FieldPosition fp = new FieldPosition(0);
				StringBuffer strbuf = sFormat.format(date, buftmp, fp);
				sToday = strbuf.toString();

				sSQL.delete(nBegin, nEnd + 2);
				sSQL.insert(nBegin, sToday);
				nCur = nBegin + sToday.length();
				// n日前(YYYY-MM-DD)
			}
			else if (sMacro.indexOf("YYYYMMDD_1") >= 0) {
				String sToday;
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
				java.util.Date date = new java.util.Date();
				StringBuffer buftmp = new StringBuffer(10);
				FieldPosition fp = new FieldPosition(0);
				StringBuffer strbuf = sFormat.format(date, buftmp, fp);
				sToday = strbuf.toString();

				sSQL.delete(nBegin, nEnd + 2);
				sSQL.insert(nBegin, sToday);
				nCur = nBegin + sToday.length();
				// 当日(YYYY-MM-DD)
			} else if (sMacro.indexOf("YYYY-MM-DD_5") >= 0) {
				String sToday;
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = new java.util.Date();
				StringBuffer buftmp = new StringBuffer(10);
				FieldPosition fp = new FieldPosition(0);
				StringBuffer strbuf = sFormat.format(date, buftmp, fp);
				sToday = strbuf.toString();

				sSQL.delete(nBegin, nEnd + 2);
				sSQL.insert(nBegin, sToday);
				nCur = nBegin + sToday.length();
				// n日前(YYYYMMDD)
			} else if (sMacro.indexOf("YYYY-MM-DD_4") >= 0) {
				int i = sMacro.indexOf('[', 0);
				int j = sMacro.indexOf(']', i);
				String sTemp = sMacro.substring(i + 1, j);
				sTemp = sTemp.trim();
				int n = Integer.parseInt(sTemp);
				String sToday;
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				;
				calendar.add(Calendar.DATE, -n);
				java.util.Date date = calendar.getTime();
				StringBuffer buftmp = new StringBuffer(10);
				FieldPosition fp = new FieldPosition(0);
				StringBuffer strbuf = sFormat.format(date, buftmp, fp);
				sToday = strbuf.toString();

				sSQL.delete(nBegin, nEnd + 2);
				sSQL.insert(nBegin, sToday);
				nCur = nBegin + sToday.length();
			}
		}
		return sSQL.toString();

	}
}
