package com.sunyard.ars.common.comm;

import com.sunyard.ars.common.pojo.DelRelate;
import com.sunyard.cloud.util.JwtUtil;

/**
 * 
 * @date 2018年6月20日
 * @Description ARS初始化参数管理类 TODO 程序初始化参数设置
 * 
 */
public class ARSInit {
	public void init() {
		// 初始化表关联关系配置
//		try {
//			System.out.println("\n\n\n\\n\njwt inti ");
//			JwtUtil.createJWT("11111111111111111", 1, "admin", "9999", null);
//			System.out.println("jwt inti ok \n\n\\n\n\\n\n");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		new DelRelate();
	}
}
