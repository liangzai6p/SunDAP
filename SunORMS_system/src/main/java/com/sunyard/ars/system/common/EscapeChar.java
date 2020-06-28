package com.sunyard.ars.system.common;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Title:EscapeChar.java</p>
 * <p>Description: 转义字符</p>	
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Sunyard System Engineering Co.,Ltd.</p>
 * @author 黎灵青
 * @date 2010-2-1
 * @version 1.0
 */
public class EscapeChar {
	/**
	 * 需转义字符集合
	 */
	public static Set<String> ESCAPE_CHAR_SET = new HashSet<String>();
	
	static {
		ESCAPE_CHAR_SET.add(".");
		ESCAPE_CHAR_SET.add("$");
		ESCAPE_CHAR_SET.add("^");
		ESCAPE_CHAR_SET.add("{");
		ESCAPE_CHAR_SET.add("[");
		ESCAPE_CHAR_SET.add("(");
		ESCAPE_CHAR_SET.add("|");
		ESCAPE_CHAR_SET.add(")");
		ESCAPE_CHAR_SET.add("*");
		ESCAPE_CHAR_SET.add("+");
		ESCAPE_CHAR_SET.add("?");
		ESCAPE_CHAR_SET.add("\\");
	}
}
