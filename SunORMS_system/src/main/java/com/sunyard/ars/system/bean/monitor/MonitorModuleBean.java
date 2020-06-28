package com.sunyard.ars.system.bean.monitor;

import java.io.Serializable;

/**
 * ... 实体类
 * 
 * @author:	lx
 * @date:	2018年12月26日 17:28:34
 */
public class MonitorModuleBean implements Serializable {

	/** 序列号 */
	private static final long serialVersionUID = -2086903327031940163L;
	
	/** 存储过程标识 */
	private String ser_flag;

	public String getSer_flag() {
		return ser_flag;
	}

	public void setSer_flag(String ser_flag) {
		this.ser_flag = ser_flag;
	}
}
